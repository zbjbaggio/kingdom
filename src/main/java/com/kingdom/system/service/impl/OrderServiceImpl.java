package com.kingdom.system.service.impl;

import com.kingdom.system.data.dto.OrderDTO;
import com.kingdom.system.data.dto.OrderDetailDTO;
import com.kingdom.system.data.dto.OrderExpressDTO;
import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.*;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.OrderDetailVO;
import com.kingdom.system.data.vo.OrderVO;
import com.kingdom.system.data.vo.ProductPackageVO;
import com.kingdom.system.data.vo.ProductVO;
import com.kingdom.system.mapper.*;
import com.kingdom.system.util.BigDecimalUtils;
import com.kingdom.system.util.DateUtil;
import com.kingdom.system.util.NOUtils;
import com.kingdom.system.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl {

    @Inject
    private OrderInfoMapper orderInfoMapper;

    @Inject
    private OrderProductMapper orderProductMapper;

    @Inject
    private OrderPaymentMapper orderPaymentMapper;

    @Inject
    private UserMapper userMapper;

    @Inject
    private ProductMapper productMapper;

    @Inject
    private ExchangeRateRecordMapper exchangeRateRecordMapper;

    @Inject
    private ProductServiceImpl productService;

    @Inject
    private OrderDetailMapper orderDetailMapper;

    @Inject
    private OrderExpressMapper orderExpressMapper;

    @Inject
    private OrderExpressDetailMapper orderExpressDetailMapper;

    @Inject
    private OrderUserMapper orderUserMapper;

    @Inject
    private OrderParentMapper orderParentMapper;

    @Transactional
    public OrderDTO insert(OrderDTO orderDTO) {
        Map<Long, ProductVO> productNameMap = checkOrder(orderDTO);
        OrderInfo orderInfo = orderDTO.getOrderInfo();
        orderInfo.setStatus(1);
        orderInfo.setOrderNo(NOUtils.getGeneratorNO());
        orderInfo.setDate(DateUtil.date());
        int count = orderInfoMapper.insertOrderInfo(orderInfo);
        if (count != 1) {
            log.error("订单保存失败！orderDTO：{}", orderDTO);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        saveOrderProduct(orderDTO.getOrderUsers(), orderInfo.getId(), productNameMap);
        List<OrderPayment> orderPayments = orderDTO.getOrderPayments();
        for (OrderPayment orderPayment : orderPayments) {
            orderPayment.setOrderId(orderInfo.getId());
        }
        orderPaymentMapper.insertOrderPayments(orderPayments);
        return orderDTO;
    }

    private void saveOrderProduct(List<OrderUser> orderUsers, Long orderId, Map<Long, ProductVO> productMap) {
        ExchangeRateRecord exchangeRateRecord = exchangeRateRecordMapper.selectDefault();
        BigDecimal rate = exchangeRateRecord.getRate();
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail = null;
        for (OrderUser orderUser : orderUsers) {
            setUserId(orderUser);
            orderUser.setOrderId(orderId);
            orderUserMapper.insertOrderUser(orderUser);
            for (OrderProduct orderProduct : orderUser.getOrderProducts()) {
                orderProduct.setOrderId(orderId);
                ProductVO productVO = productMap.get(orderProduct.getProductId());
                orderProduct.setProductName(productVO.getName());
                orderProduct.setExchangeRate(rate);
                orderProduct.setOrderUserId(orderUser.getId());
                orderProduct.setExchangeRateId(exchangeRateRecord.getId());
                orderProduct.setHkCostPrice(productVO.getCostPrice());
                orderProduct.setHkSellingPrice(productVO.getSellingPrice());
                orderProduct.setHkCostAmount(BigDecimalUtils.multiply(productVO.getSellingPrice(), orderProduct.getNumber()));
                orderProduct.setHkAmount(BigDecimalUtils.multiply(productVO.getCostPrice(), orderProduct.getNumber()));
                orderProduct.setCnyCostPrice(productVO.getCostPrice().multiply(rate));
                orderProduct.setCnySellingPrice(productVO.getSellingPrice().multiply(rate));
                orderProduct.setCnyCostAmount(BigDecimalUtils.multiply(productVO.getCostPrice(), orderProduct.getNumber()).multiply(rate));
                orderProduct.setCnyAmount(BigDecimalUtils.multiply(productVO.getSellingPrice(), orderProduct.getNumber()).multiply(rate));
                orderProduct.setScore(productVO.getScore() * orderProduct.getNumber());
                orderProductMapper.insertOrderProduct(orderProduct);
                // 单品直接存入 产品包存入明细
                List<ProductPackageVO> productPackageVOList = productVO.getProductPackageVOList();
                if (productPackageVOList != null && productPackageVOList.size() > 0) {
                    for (ProductPackageVO productPackageVO : productPackageVOList) {
                        orderDetail = new OrderDetail();
                        orderDetail.setOrderId(orderId);
                        orderDetail.setOrderProductId(orderProduct.getId());
                        orderDetail.setProductId(productPackageVO.getProductBId());
                        orderDetail.setProductName(productPackageVO.getName());
                        orderDetail.setProductPackageId(productPackageVO.getProductId());
                        orderDetail.setNumber(productPackageVO.getNumber() * orderProduct.getNumber());
                        orderDetail.setOrderUserId(orderUser.getId());
                        orderDetails.add(orderDetail);
                    }
                } else {
                    orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderId);
                    orderDetail.setOrderProductId(orderProduct.getId());
                    orderDetail.setProductId(orderProduct.getProductId());
                    orderDetail.setProductName(orderProduct.getProductName());
                    orderDetail.setProductPackageId(-1L);
                    orderDetail.setNumber(orderProduct.getNumber());
                    orderDetail.setOrderUserId(orderUser.getId());
                    orderDetails.add(orderDetail);
                }
                //orderProduct.getp
            }
        }
        orderDetailMapper.insertOrderDetails(orderDetails);
        //orderProductMapper.insertOrderProducts(orderProducts);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setUserId(OrderUser orderUser) {
        if (StringUtils.isNoneEmpty(orderUser.getUserNo())) {
            UserEntity userEntity = userMapper.selectUserByMemberNo(orderUser.getUserNo());
            if (userEntity == null) {
                userEntity = new UserEntity();
                userEntity.setRealName(orderUser.getUserName());
                userEntity.setMemberNo(orderUser.getUserNo());
                userMapper.insertUser(userEntity);
            }
            orderUser.setUserId(userEntity.getId());
        }
    }

    //返回产品名称map
    private Map<Long, ProductVO> checkOrder(OrderDTO orderDTO) {
        List<OrderUser> orderUsers = orderDTO.getOrderUsers();
        Set<Long> productIds = new HashSet<>();
        for (OrderUser orderUser : orderUsers) {
            List<OrderProduct> orderProducts = orderUser.getOrderProducts();
            for (OrderProduct orderProduct : orderProducts) {
                productIds.add(orderProduct.getProductId());
            }
        }
        List<ProductVO> productVOList = productMapper.listProductByIds(productIds);
        if (productVOList.size() != productIds.size()) {
            log.error("产品id和查询出产品数量不一致 orderDTO:{}", orderDTO);
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        productService.getPackage(productVOList);
        Map<Long, ProductVO> map = new HashMap<>();
        for (ProductVO productVO : productVOList) {
            map.put(productVO.getId(), productVO);
        }
        return map;
    }

    public List<OrderInfo> list(String payUser, String orderUser, String express, String startDate, String endDate) {
        return orderInfoMapper.selectOrderInfoList(payUser, orderUser, express, startDate, endDate);
    }

    public OrderVO detail(Long orderId) {
        OrderVO orderVO = new OrderVO();
        OrderInfo orderInfo = orderInfoMapper.selectOrderDetailInfoById(orderId);
        orderVO.setOrderInfo(orderInfo);
        List<OrderUser> orderUserVOs = orderUserMapper.selectOrderUserListByOrderId(orderId);
        getOrderProduct(orderUserVOs, orderId);
        orderVO.setOrderDetails(getOrderDetails(orderUserVOs, orderId));
        orderVO.setOrderPayments(orderPaymentMapper.selectOrderPaymentListByOrderId(orderId));
        setExpressInfo(orderId, orderVO);
        return orderVO;
    }

    private List<OrderUser> getOrderProduct(List<OrderUser> orderProduct, Long orderId) {
        List<OrderProduct> orderProducts = orderProductMapper.selectOrderProductListByOrderId(orderId);
        List<OrderDetailVO> orderDetailVOS = orderDetailMapper.selectOrderDetailPackageListByOrderId(orderId);
        Map<Long, List<OrderDetailVO>> map = new HashMap<>();
        for (OrderDetailVO orderDetailVO : orderDetailVOS) {
            List<OrderDetailVO> voList = map.get(orderDetailVO.getOrderProductId());
            if (voList == null) {
                voList = new ArrayList<>();
            }
            voList.add(orderDetailVO);
            map.put(orderDetailVO.getOrderProductId(), voList);
        }
        Map<Long, List<OrderProduct>> productMap = new HashMap<>();
        for (OrderProduct product : orderProducts) {
            List<OrderProduct> voList = productMap.get(product.getOrderUserId());
            if (voList == null) {
                voList = new ArrayList<>();
            }
            product.setOrderDetailVOs(map.get(product.getId()));
            voList.add(product);
            productMap.put(product.getOrderUserId(), voList);
        }
        for (OrderUser orderUser : orderProduct) {
            orderUser.setOrderProducts(productMap.get(orderUser.getId()));
        }
        return orderProduct;
    }

    private List<OrderUser> getOrderDetails(List<OrderUser> orderUserVOs, Long orderId) {
        List<OrderDetailVO> orderDetailVOs = orderDetailMapper.selectOrderDetailListByOrderId(orderId);
        Map<Long, List<OrderDetailVO>> productMap = new HashMap<>();
        for (OrderDetailVO orderDetailVO : orderDetailVOs) {
            Long key = orderDetailVO.getOrderUserId();
            List<OrderDetailVO> productVOs = productMap.get(key);
            if (productVOs == null) {
                productVOs = new ArrayList<>();
            }
            productVOs.add(orderDetailVO);
            productMap.put(key, productVOs);
        }
        for (OrderUser orderUser : orderUserVOs) {
            orderUser.setOrderDetailVOs(productMap.get(orderUser.getId()));
        }
        return orderUserVOs;
    }

    private void setExpressInfo(Long orderId, OrderVO orderVO) {
        List<OrderExpress> orderExpresses = orderExpressMapper.selectOrderExpressListByOrderId(orderId);
        List<OrderExpressDetail> orderExpressesDetails = orderExpressDetailMapper.selectOrderExpressDetailListByOrderId(orderId);
        Map<Long, List<OrderExpressDetail>> map = new HashMap<>();
        for (OrderExpressDetail orderExpressesDetail : orderExpressesDetails) {
            Long orderExpressId = orderExpressesDetail.getOrderExpressId();
            List<OrderExpressDetail> orderExpressDetails = map.get(orderExpressId);
            if (orderExpressDetails == null) {
                orderExpressDetails = new ArrayList<>();
            }
            orderExpressDetails.add(orderExpressesDetail);
            map.put(orderExpressId, orderExpressDetails);
        }
        for (OrderExpress orderExpress : orderExpresses) {
            List<OrderExpressDetail> orderExpressDetails = map.get(orderExpress.getId());
            /*StringBuilder productDetail = new StringBuilder();
            for (OrderExpressDetail orderExpressDetail : orderExpressDetails) {
                productDetail.append(orderExpressDetail.getProductName()).append(" : ").append(orderExpressDetail.getNumber()).append("\n");
            }
            orderExpress.setProductDetail(productDetail.toString());*/
            orderExpress.setUserName(orderExpressDetails.get(0).getUserName());
            orderExpress.setOrderExpressDetails(orderExpressDetails);
        }
        orderVO.setOrderExpresses(orderExpresses);
    }

    public OrderDTO update(OrderDTO orderDTO) {
        OrderInfo oldOrderDTO = orderInfoMapper.selectOrderInfoById(orderDTO.getOrderInfo().getId());
        if (oldOrderDTO.getExpress() != 0) {
            log.error("订单已经有快递信息不能修改！orderDTO：{}", orderDTO);
            throw new PrivateException(ErrorInfo.ORDER_UPDATE_ERROR);
        }
        Map<Long, ProductVO> productNameMap = checkOrder(orderDTO);
        OrderInfo orderInfo = orderDTO.getOrderInfo();
        int count = orderInfoMapper.updateOrderInfo(orderInfo);
        if (count != 1) {
            log.error("订单修改失败！orderDTO：{}", orderDTO);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        Long orderId = orderInfo.getId();
        orderProductMapper.deleteOrderProductByOrderId(orderId);
        orderDetailMapper.deleteOrderDetailByOrderId(orderId);
        saveOrderProduct(orderDTO.getOrderUsers(), orderId, productNameMap);
        List<OrderPayment> orderPayments = orderDTO.getOrderPayments();
        for (OrderPayment orderPayment : orderPayments) {
            orderPayment.setOrderId(orderInfo.getId());
        }
        orderPaymentMapper.deleteOrderPaymentByOrderId(orderId);
        orderPaymentMapper.insertOrderPayments(orderPayments);
        return orderDTO;
    }

    @Transactional
    public OrderExpressDTO insertExpress(OrderExpressDTO orderExpressDTO) {
        OrderExpress orderExpress = orderExpressDTO.getOrderExpress();
        List<OrderDetailVO> orderDetailVOS = orderDetailMapper.selectExpressByOrderIdAndUserId(orderExpress.getOrderId(), orderExpress.getOrderUserId());
        if (orderDetailVOS == null || orderDetailVOS.size() == 0) {
            log.error("订单信息错误！orderExpressDTO:{}", orderExpressDTO);
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        List<OrderExpressDetail> orderExpressDetailList = orderExpressDTO.getExpressDetails();
        orderExpressMapper.insertOrderExpress(orderExpress);
        //修改出货数量
        Map<Long, List<OrderDetailVO>> productMap = new HashMap<>();
        for (OrderDetailVO orderDetailVO : orderDetailVOS) {
            Long key = orderDetailVO.getProductId();
            List<OrderDetailVO> result = productMap.get(key);
            if (result == null) {
                result = new ArrayList<>();
            }
            result.add(orderDetailVO);
            productMap.put(key, result);
        }
        for (OrderExpressDetail orderExpressDetail : orderExpressDetailList) {
            orderExpressDetail.setOrderExpressId(orderExpress.getId());
            orderExpressDetail.setOrderUserId(orderExpress.getOrderUserId());
            orderExpressDetail.setOrderId(orderExpress.getOrderId());
            List<OrderDetailVO> result = productMap.get(orderExpressDetail.getProductId());
            int expressNumber = orderExpressDetail.getNumber();
            if (result != null) {
                orderExpressDetail.setProductName(result.get(0).getProductName());
                for (OrderDetailVO orderDetailVO : result) {
                    int number = orderDetailVO.getNumber() - orderDetailVO.getExpressNumber();
                    if (number >= expressNumber) {
                        updateNumber(orderDetailVO, expressNumber);
                        expressNumber = expressNumber - number;
                        if (expressNumber <= 0) {
                            break;
                        }
                    } else {
                        updateNumber(orderDetailVO, number);
                        expressNumber = expressNumber - number;
                    }
                }
                if (expressNumber > 0) {
                    log.error("发货数量大于下单数量！");
                    throw new PrivateException(ErrorInfo.UPDATE_ERROR);
                }
            } else {
                log.error("产品未找到！orderExpressDTO：{}", orderExpressDTO);
                throw new PrivateException(ErrorInfo.PARAMS_ERROR);
            }
        }
        orderExpressDetailMapper.insertOrderExpressDetails(orderExpressDetailList);
        //修改商品库存数量
        Map<Long, Integer> productNumberMap = new HashMap<>();
        for (Map.Entry<Long, List<OrderDetailVO>> entry : productMap.entrySet()) {
            List<OrderDetailVO> result = productMap.get(entry.getKey());
            Integer number = 0;
            for (OrderDetailVO orderDetailVO : result) {
                number += orderDetailVO.getNumber();
            }
            productNumberMap.put(entry.getKey(), number);
        }
        modifyProductNumber(productNumberMap);
        return orderExpressDTO;
    }

    private void modifyProductNumber(Map<Long, Integer> productNumberMap) {
        productMapper.updateNumber(productNumberMap);
    }

    private void updateNumber(OrderDetailVO orderDetailVO, int number) {
        int count = orderDetailMapper.updateNumber(orderDetailVO.getId(), number);
        if (count != 1) {
            log.error("修改发货数量失败！");
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
    }

    @Transactional
    public OrderExpressDTO updateExpress(OrderExpressDTO orderExpressDTO) {
        OrderExpress orderExpress = orderExpressDTO.getOrderExpress();
        List<OrderDetailVO> orderDetailVOS = orderDetailMapper.selectExpressByOrderIdExpress(orderExpress.getOrderId(), orderExpress.getOrderUserId());
        if (orderDetailVOS == null || orderDetailVOS.size() == 0) {
            log.error("订单信息错误！orderExpressDTO:{}", orderExpressDTO);
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        List<OrderExpressDetail> orderExpressDetailList = orderExpressDTO.getExpressDetails();
        orderExpressMapper.updateOrderExpress(orderExpress);
        List<OrderExpressDetail> orderExpressDetailListOld = orderExpressDetailMapper.selectOrderExpressDetailListByExpressId(orderExpress.getId());
        orderExpressDetailListOld.addAll(orderExpressDTO.getExpressDetails());
        Map<Long, Integer> map = new HashMap<>();
        for (OrderExpressDetail orderExpressDetail : orderExpressDetailListOld) {
            Integer number = map.get(orderExpressDetail.getProductId());
            if (number == null) {
                number = 0;
            }
            number += orderExpressDetail.getNumber();
            map.put(orderExpressDetail.getProductId(), number);
        }

        Map<Long, List<OrderDetailVO>> productMap = new HashMap<>();
        for (OrderDetailVO orderDetailVO : orderDetailVOS) {
            Long key = orderDetailVO.getProductId();
            List<OrderDetailVO> result = productMap.get(key);
            if (result == null) {
                result = new ArrayList<>();
            }
            result.add(orderDetailVO);
            productMap.put(key, result);
        }
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            List<OrderDetailVO> result = productMap.get(entry.getKey());
            int expressNumber = entry.getValue();
            if (result != null) {
                for (OrderDetailVO orderDetailVO : result) {
                    if (expressNumber > 0) {
                        int number = orderDetailVO.getNumber() - orderDetailVO.getExpressNumber();
                        if (number >= expressNumber) {
                            updateNumber(orderDetailVO, expressNumber);
                            expressNumber = 0;
                            break;
                        } else {
                            updateNumber(orderDetailVO, number);
                            expressNumber = expressNumber - number;
                        }
                    } else {
                        if (orderDetailVO.getExpressNumber() >= -expressNumber) {
                            updateNumber(orderDetailVO, expressNumber);
                            expressNumber = 0;
                            break;
                        } else {
                            updateNumber(orderDetailVO, orderDetailVO.getExpressNumber());
                            expressNumber = expressNumber +  orderDetailVO.getExpressNumber();
                        }
                    }
                }
                if (expressNumber != 0) {
                    log.error("发货数量大于下单数量！");
                    throw new PrivateException(ErrorInfo.UPDATE_ERROR);
                }
            }
        }
        for (OrderExpressDetail orderExpressDetail : orderExpressDetailList) {
            orderExpressDetail.setOrderExpressId(orderExpress.getId());
            List<OrderDetailVO> result = productMap.get(orderExpressDetail.getProductId());
            orderExpressDetail.setProductName(result.get(0).getProductName());
            orderExpressDetail.setOrderUserId(orderExpress.getOrderUserId());
            orderExpressDetail.setOrderId(orderExpress.getOrderId());
        }
        modifyProductNumber(map);
        orderExpressDetailMapper.deleteOrderExpressDetailByExpressId(orderExpress.getId());
        orderExpressDetailMapper.insertOrderExpressDetails(orderExpressDetailList);
        return null;
    }

    public void printNumber(Long orderExpressId) {
        orderExpressMapper.updatePrintNumber(orderExpressId);
    }

    public void updateDetail(OrderDetailDTO orderDetailDTO) {
        orderInfoMapper.updateDetail(orderDetailDTO.getId(), orderDetailDTO.getRemark());
    }

    public List<OrderDetailVO> listProductByIds(List<Long> orderIds) {
        return orderDetailMapper.listProductByIds(orderIds);
    }

    public List<OrderUser> listUserByIds(List<Long> orderIds) {
        return orderUserMapper.listUserByIds(orderIds);
    }

    public List<OrderPayment> listOrderPaymentByIds(List<Long> orderIds) {
        return orderPaymentMapper.listPaymentByIds(orderIds);
    }

    public void orderParentSave(OrderParent orderParent) {
        orderParentMapper.insertOrderParent(orderParent);
    }

    public List<OrderParent> listOrderParent(String name) {
        return orderParentMapper.selectOrderParentListByName(name);
    }

    @Transactional
    public void doneOrder(OrderParent orderParent) {
        orderParentSave(orderParent);
        //计算积分
/*        List<OrderProduct> list = orderProductMapper.selectUserSore();
        userMapper.updateScore(list);*/
        updateOrderParentId(orderParent.getId());
    }

    private void updateOrderParentId(Long orderParentId) {
        orderInfoMapper.updateOrderParentId(orderParentId);
    }

/*    private void checkProductExpress(List<OrderDetail> orderDetails, List<OrderExpress> orderExpresses) {
        for () {
        }

    }*/
}
