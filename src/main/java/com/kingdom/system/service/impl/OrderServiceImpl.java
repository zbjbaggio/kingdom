package com.kingdom.system.service.impl;

import com.kingdom.system.data.dto.OrderDTO;
import com.kingdom.system.data.dto.OrderExpressDTO;
import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.*;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.*;
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
    private OrderExpressDetailMapper expressDetailMapper;

    @Inject
    private OrderUserMapper orderUserMapper;

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

    public List<?> list(String search, String sendDateStart, String sendDateEnd) {
        return orderInfoMapper.selectOrderInfoList(null);
    }

    public OrderVO detail(Long orderId) {
        OrderVO orderVO = new OrderVO();
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        orderVO.setOrderInfo(orderInfo);
        orderVO.setOrderDetails(getOrderDetails(orderId));
        orderVO.setOrderProducts(orderProductMapper.selectOrderProductListByOrderId(orderId));
        orderVO.setOrderPayments(orderPaymentMapper.selectOrderPaymentListByOrderId(orderId));
        setExpressInfo(orderId, orderVO);
        return orderVO;
    }

    private List<OrderUser> getOrderDetails(Long orderId) {
        List<OrderUser> orderUserVOs = orderUserMapper.selectOrderUserListByOrderId(orderId);
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
        List<OrderExpressDetail> orderExpressesDetails = expressDetailMapper.selectOrderExpressDetailListByOrderId(orderId);
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
            StringBuffer productDetail = new StringBuffer();
            for (OrderExpressDetail orderExpressDetail : orderExpressDetails) {
                productDetail.append(orderExpressDetail.getProductName()).append(" : ").append(orderExpressDetail.getNumber()).append("\n");
            }
            orderExpress.setProductDetail(productDetail.toString());
        }
        orderVO.setOrderExpresses(orderExpresses);
    }

    public OrderDTO update(OrderDTO orderDTO) {
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

    public OrderDTO insertExpress(OrderExpressDTO orderExpressDTO) {



        return null;
    }

/*    private void checkProductExpress(List<OrderDetail> orderDetails, List<OrderExpress> orderExpresses) {
        for () {
        }

    }*/
}
