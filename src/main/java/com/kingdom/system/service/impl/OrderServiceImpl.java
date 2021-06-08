package com.kingdom.system.service.impl;

import com.kingdom.system.data.dto.*;
import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.*;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.OrderDetailVO;
import com.kingdom.system.data.vo.OrderVO;
import com.kingdom.system.data.vo.ProductPackageVO;
import com.kingdom.system.data.vo.ProductVO;
import com.kingdom.system.mapper.*;
import com.kingdom.system.util.*;
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

    @Inject
    private UserSendAddressMapper userSendAddressMapper;

    @Inject
    private ValueHolder valueHolder;

    @Transactional
    public OrderDTO insert(OrderDTO orderDTO) {
        checkPay(orderDTO.getOrderPayments(), null);
        //校验同时存入userId
        Map<Long, ProductVO> productNameMap = checkOrder(orderDTO);
        OrderInfo orderInfo = orderDTO.getOrderInfo();
        orderInfo.setStatus(1);
        int count = orderInfoMapper.insertOrderInfo(orderInfo);
        orderInfo.setOrderNo(NOUtils.getGeneratorNO(orderInfo.getId()));
        orderInfoMapper.updateOrderNO(orderInfo.getId(), orderInfo.getOrderNo());
        if (count != 1) {
            log.error("订单保存失败！orderDTO：{}", orderDTO);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        saveOrderProduct(orderDTO.getOrderUsers(), orderInfo.getId(), productNameMap);
        List<OrderPayment> orderPayments = orderDTO.getOrderPayments();
        if (orderPayments.get(0).getPayAmount() != null) {
            for (OrderPayment orderPayment : orderPayments) {
                orderPayment.setOrderId(orderInfo.getId());
            }
            orderInfoMapper.updateOrderStatus(orderInfo.getId(), 3);
            orderPaymentMapper.insertOrderPayments(orderPayments);
        }
        return orderDTO;
    }

    private void checkPay(List<OrderPayment> orderPayments, Long orderId) {
        Set set = new HashSet();
        List list = new ArrayList();
        orderPayments.forEach(orderPayment -> {
            if (orderPayment.getPayType() == 0 && StringUtils.isNoneEmpty(orderPayment.getPayNo())) {
                set.add(orderPayment.getPayNo());
                list.add(orderPayment.getPayNo());
            }
        });
        if (list.size() > 0) {
            if (list.size() != set.size()) {
                throw new PrivateException(ErrorInfo.CARD_SAME);
            }
            int count = orderPaymentMapper.selectOrderUserListByPayNo(list, orderId);
            if (count > 0) {
                throw new PrivateException(ErrorInfo.CARD_SAME2);
            }
        }

    }

    public void saveOrderProduct(List<OrderUser> orderUsers, Long orderId, Map<Long, ProductVO> productMap) {
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
                orderProduct.setHkCostAmount(BigDecimalUtils.multiply(productVO.getCostPrice(), orderProduct.getNumber()));
                orderProduct.setHkAmount(BigDecimalUtils.multiply(productVO.getSellingPrice(), orderProduct.getNumber()));
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

    @Transactional
    public void setUserId(OrderUser orderUser) {
        if (StringUtils.isNoneEmpty(orderUser.getUserNo())) {
            UserEntity userEntity = userMapper.selectUserByMemberNo(orderUser.getUserNo());
            if (userEntity == null) {
                userEntity = new UserEntity();
                userEntity.setRealName(orderUser.getUserName());
                userEntity.setMemberNo(orderUser.getUserNo());
                userMapper.insertUser(userEntity);
            } else {
                if (!userEntity.getRealName().equals(orderUser.getUserName())) {
                    throw new PrivateException(5004, "订货人" + orderUser.getUserNo() + "的会员号与姓名不一致！");
                }
            }
            orderUser.setUserId(userEntity.getId());
        }
    }

    //返回产品名称map
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<Long, ProductVO> checkOrder(OrderDTO orderDTO) {
        //校验下单人
        OrderInfo orderInfo = orderDTO.getOrderInfo();
        if (orderInfo.getUserId() == null) {
            UserEntity userEntity = userMapper.selectUserByMemberNo(orderInfo.getMemberNo());
            if (userEntity != null) {
                if (!userEntity.getRealName().equals(orderInfo.getOrderUsername())) {
                    log.error("用户名和会员号不一致！orderDTO:{}", orderDTO);
                    throw new PrivateException(5004, "付款人" + orderInfo.getMemberNo() + "的会员号与姓名不一致！");
                }
                orderInfo.setUserId(userEntity.getId());
            } else {
                UserEntity newUserEntity = new UserEntity();
                newUserEntity.setMemberNo(orderInfo.getMemberNo());
                newUserEntity.setMobile(orderInfo.getOrderPhone());
                newUserEntity.setRealName(orderInfo.getOrderUsername());
                int count = userMapper.insertUser(newUserEntity);
                if (count != 1) {
                    log.error("用户存盘失败！userEntity:{}", newUserEntity);
                    throw new PrivateException(ErrorInfo.ORDER_ERROR);
                }
                orderInfo.setUserId(newUserEntity.getId());
            }
        }
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

    public List<OrderInfo> list(Long userId, String payUser, String orderUser, String express, String productName,
                                String status,
                                String startDate, String endDate) {
        Date date = DateUtil.addHour(new Date(), -6);
        return orderInfoMapper.selectOrderInfoList(userId, payUser, orderUser, express, productName, status, DateUtil.formatTime(date), startDate, endDate);
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

    @Transactional
    public OrderDTO update(OrderDTO orderDTO) {
        if (orderDTO.getOrderInfo().getId() == null) {
            log.info("订单修改id不能为空！");
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        checkPay(orderDTO.getOrderPayments(), orderDTO.getOrderInfo().getId());
        checkOrderExpress(orderDTO.getOrderInfo().getId());
        Map<Long, ProductVO> productNameMap = checkOrder(orderDTO);
        OrderInfo orderInfo = orderDTO.getOrderInfo();
        int count = orderInfoMapper.updateOrderInfo(orderInfo);
        if (count != 1) {
            log.error("订单修改失败！orderDTO：{}", orderDTO);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        Long orderId = orderInfo.getId();
        orderUserMapper.deleteOrderUserByOrderId(orderId);
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

    private void checkOrderExpress(Long orderId) {
        OrderInfo oldOrderDTO = orderInfoMapper.selectOrderInfoById(orderId);
        if (oldOrderDTO.getSend() != 0) {
            log.error("订单已经有快递信息不能修改！orderID：{}", orderId);
            throw new PrivateException(ErrorInfo.ORDER_UPDATE_ERROR);
        }
    }

    @Transactional
    public OrderExpressDTO insertExpress(OrderExpressDTO orderExpressDTO) {
        //校验是否有订货人没有填写会员号
        OrderExpress orderExpress = orderExpressDTO.getOrderExpress();
        List<OrderUser> orderUsers = orderUserMapper.selectOrderUserListByOrderId(orderExpress.getOrderId());
        StringBuilder stringBuilder = new StringBuilder();
        for (OrderUser orderUser : orderUsers) {
            if (orderUser.getUserId() == -1L) {
                stringBuilder.append(orderUser.getUserName()).append(",");
            }
        }
        if (stringBuilder.length() > 0) {
            String msg = stringBuilder.substring(0, stringBuilder.length() - 1);
            PrivateException privateException = new PrivateException();
            privateException.setCode(5004);
            privateException.setMsg(msg + "没有会员号");
            throw privateException;
        }
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
        Map<Long, Integer> productNumberMap = new HashMap<>();
        for (OrderExpressDetail orderExpressDetail : orderExpressDetailList) {
            orderExpressDetail.setOrderExpressId(orderExpress.getId());
            orderExpressDetail.setOrderUserId(orderExpress.getOrderUserId());
            orderExpressDetail.setOrderId(orderExpress.getOrderId());
            List<OrderDetailVO> result = productMap.get(orderExpressDetail.getProductId());
            productNumberMap.put(orderExpressDetail.getProductId(), orderExpressDetail.getNumber());
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
        orderInfoMapper.updateSend(1, orderExpress.getOrderId());
        saveExpress(orderExpress);
        orderExpressDetailMapper.insertOrderExpressDetails(orderExpressDetailList);
        //修改商品库存数量
        modifyProductNumber(productNumberMap);
        return orderExpressDTO;
    }

    private void checkUserId() {
        List<OrderInfo> list = orderInfoMapper.getCountNoUserId();
        if (list != null && list.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (OrderInfo orderInfo : list) {
                stringBuilder.append(orderInfo.getOrderNo()).append(",");
            }
            PrivateException exception = new PrivateException();
            exception.setCode(50002);
            String msg = stringBuilder.toString();
            exception.setMsg("以下订单的订货人未填写会员号：" + msg.substring(0, msg.length() - 1));
            throw exception;
        }
    }

    private void saveExpress(OrderExpress orderExpress) {
        // 如果没有常用地址id就保存一份新的
        if (orderExpress.getUserSendAddressId() == null || orderExpress.getUserSendAddressId() == 0L) {
            UserSendAddress userSendAddress = new UserSendAddress();
            userSendAddress.setAddress(orderExpress.getExpressAddress());
            userSendAddress.setUserId(orderExpress.getOrderUserId());
            userSendAddress.setMobile(orderExpress.getExpressPhone());
            userSendAddress.setTakedName(orderExpress.getExpressName());
            userSendAddressMapper.insertUserSendAddress(userSendAddress);
        }
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
        //查询数据将数量转为负值
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
                            expressNumber = expressNumber + orderDetailVO.getExpressNumber();
                        }
                    }
                }
                if (expressNumber != 0) {
                    log.error("发货数量大于下单数量！");
                    throw new PrivateException(ErrorInfo.UPDATE_ERROR);
                }
            }
        }
        Map<Long, Integer> productNumberMap = new HashMap<>();
        for (OrderExpressDetail orderExpressDetail : orderExpressDetailList) {
            orderExpressDetail.setOrderExpressId(orderExpress.getId());
            List<OrderDetailVO> result = productMap.get(orderExpressDetail.getProductId());
            orderExpressDetail.setProductName(result.get(0).getProductName());
            orderExpressDetail.setOrderUserId(orderExpress.getOrderUserId());
            orderExpressDetail.setOrderId(orderExpress.getOrderId());
            productNumberMap.put(orderExpressDetail.getProductId(), orderExpressDetail.getNumber());
        }
        modifyProductNumber(map);
        saveExpress(orderExpress);
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
        //校验是否有订货人没有填写会员号
        checkUserId();
        orderParentSave(orderParent);
        //计算积分
/*        List<OrderProduct> list = orderProductMapper.selectUserSore();
        userMapper.updateScore(list);*/
        updateOrderParentId(orderParent.getId());
    }

    private void updateOrderParentId(Long orderParentId) {
        orderInfoMapper.updateOrderParentId(orderParentId);
    }

    @Transactional
    public void delete(Long orderId) {
        checkOrderExpress(orderId);
        orderInfoMapper.deleteOrderInfoById(orderId);
        orderUserMapper.deleteOrderUserByOrderId(orderId);
        orderProductMapper.deleteOrderProductByOrderId(orderId);
        orderDetailMapper.deleteOrderDetailByOrderId(orderId);
    }

    public OrderParent getOrderParent() {
        String now = DateUtil.formatDate();
        return orderParentMapper.selectOrderParentByDate(now);
    }

    public List<OrderInfo> getOrderParentSum() {
        return orderInfoMapper.getOrderParentSum();
    }

    public List<OrderInfo> listByUserId() {
        Long userId = valueHolder.getMobileUserHolder();
        return orderInfoMapper.selectOrderInfoListByUserId(userId);
    }

    public List<OrderInfo> detailByParentOrderId(Long parentOrderId) {
        List<OrderInfo> list = orderInfoMapper.selectOrderInfoListByParentId(parentOrderId);
        if (list != null && list.size() > 0) {
            List<Long> orderIds = new ArrayList<>();
            for (OrderInfo row : list) {
                orderIds.add(row.getId());
            }
            List<OrderDetailVO> orderProducts = listProductByIds(orderIds);
            Map<Long, List<OrderDetailVO>> orderProductMap = new HashMap<>();
            for (OrderDetailVO orderProduct : orderProducts) {
                List<OrderDetailVO> orderDetailVOS = orderProductMap.get(orderProduct.getOrderId());
                if (orderDetailVOS == null) {
                    orderDetailVOS = new ArrayList<>();
                }
                orderDetailVOS.add(orderProduct);
                orderProductMap.put(orderProduct.getOrderId(), orderDetailVOS);
            }
            List<OrderPayment> orderPayments = listOrderPaymentByIds(orderIds);
            Map<Long, List<OrderPayment>> orderPaymentMap = new HashMap<>();
            for (OrderPayment orderPayment : orderPayments) {
                List<OrderPayment> orderPaymentList = orderPaymentMap.get(orderPayment.getOrderId());
                if (orderPaymentList == null) {
                    orderPaymentList = new ArrayList<>();
                }
                orderPaymentList.add(orderPayment);
                orderPaymentMap.put(orderPayment.getOrderId(), orderPaymentList);
            }
            for (OrderInfo row : list) {
                row.setOrderDetailVOS(orderProductMap.get(row.getId()));
                row.setOrderPayments(orderPaymentMap.get(row.getId()));
            }
        }
        return list;
    }

    public List<OrderExpress> listExpressByIds(List<Long> orderIds) {
        return orderExpressMapper.selectOrderExpressListByOrderIds(orderIds);
    }

    public List<OrderExpressDetail> listExpressDetialByIds(List<Long> orderIds) {
        return orderExpressDetailMapper.selectOrderExpressDetailListByOrderIds(orderIds);
    }

    public List<OrderExcelDTO> listOrderExcel(Long orderParentId) {
        List<OrderExcelDTO> list = orderInfoMapper.selectExcel(orderParentId);
        if (list != null && list.size() > 0) {
            List<OrderDetailExcelDTO> orderDetailExcelDTOS = orderInfoMapper.selectOrderDetailExcel(orderParentId);
            Map<Long, List<OrderDetailExcelDTO>> map = new HashMap<>();
            orderDetailExcelDTOS.forEach(orderDetailExcelDTO -> {
                List<OrderDetailExcelDTO> orderDetailExcelDTOs = map.get(orderDetailExcelDTO.getOrderId());
                if (orderDetailExcelDTOs == null) {
                    orderDetailExcelDTOs = new ArrayList<>();
                }
                orderDetailExcelDTOs.add(orderDetailExcelDTO);
                map.put(orderDetailExcelDTO.getOrderId(), orderDetailExcelDTOs);
            });
            list.forEach(orderExcelDTO -> {
                orderExcelDTO.setOrderDetailExcelDTOList(map.get(orderExcelDTO.getId()));
            });
        }
        return list;
    }

    public List<OrderUser> listOrderUser(List<Long> orderIds) {
        return orderUserMapper.listUserByIds(orderIds);
    }

    public List<OrderProduct> listOrderProductByIds(List<Long> orderIds) {
        return orderProductMapper.listProductByIds(orderIds);
    }

    public List<OrderExcelPayDTO> listOrderPayExcel(Long orderParentId) {
        List<OrderExcelPayDTO> list = orderInfoMapper.selectPayExcel(orderParentId);
        if (list != null) {
            List<OrderPayment> orderPayments = orderPaymentMapper.selectOrderPaymentListByOrderParentId(orderParentId);
            Map<Long, List<OrderPayment>> map = new HashMap<>();
            orderPayments.forEach(orderPayment -> {
                switch (orderPayment.getPayType()) {
                    case 0:
                        orderPayment.setPayTypeString("港币划卡");
                        break;
                    case 1:
                        orderPayment.setPayTypeString("微信");
                        break;
                    case 2:
                        orderPayment.setPayTypeString("支付宝");
                        break;
                    case 3:
                        orderPayment.setPayTypeString("银行转账");
                        break;
                    default:
                        break;

                }
                List<OrderPayment> orderPayments1 = map.get(orderPayment.getOrderId());
                if (orderPayments1 == null) {
                    orderPayments1 = new ArrayList<>();
                }
                orderPayments1.add(orderPayment);
                map.put(orderPayment.getOrderId(), orderPayments1);
            });
            for (OrderExcelPayDTO aaa : list) {
                aaa.setOrderPayments(map.get(aaa.getId()));
            }
            return list;
        }
        return null;
    }

    public List<OrderExpress> liseExpress(String startDate, String endDate) {
        return orderExpressMapper.list(startDate, endDate);
    }

    @Transactional
    public void importExcel(List<OrderExcelImportDTO> list) {
        StringBuilder message = new StringBuilder();
        String orderNo = "";
        String payNo = "";
        // 默认港币
        int payType = 0;
        OrderPayment orderPayment;
        for (int i = 0; i < list.size(); i++) {
            OrderExcelImportDTO item = list.get(i);
            if (StringUtils.isEmpty(item.getDesc())) {
                message.append("第").append(i + 1).append("行，订单号为空！");
                break;
            }
            orderNo = item.getDesc();
            if (!StringUtils.isEmpty(item.getPayId2())) {
                payNo = item.getPayId2().trim();
            } else if (!StringUtils.isEmpty(item.getPayId())) {
                payNo = item.getPayId().trim();
            } else {
                message.append("第").append(i + 1).append("行，交易id都为空！");
                break;
            }
            OrderInfo orderInfo = orderInfoMapper.selectOrderDetailInfoByOrderNo(orderNo);
            if (orderInfo == null) {
                message.append("第").append(i + 1).append("行，该备注的订单未找到！");
                break;
            }
            List<OrderPayment> orderPayments = orderPaymentMapper.selectOrderPaymentListByOrderId(orderInfo.getId());
            if (orderPayments != null && orderPayments.size() > 0) {
                message.append("第").append(i + 1).append("行，该订单已经有了支付信息！");
            }
            // 校验金额
            List<OrderProduct> orderProducts = orderProductMapper.selectOrderProductListByOrderId(orderInfo.getId());
            BigDecimal sum = new BigDecimal("0");
            for (OrderProduct orderProduct : orderProducts) {
                sum = sum.add(orderProduct.getHkAmount());
            }
            if (sum.add(new BigDecimal(10)).compareTo(new BigDecimal(item.getSumAmount())) < 0 ||
                    sum.subtract(new BigDecimal(10)).compareTo(new BigDecimal(item.getSumAmount())) == 1) {
                message.append("第").append(i + 1).append("行，该订单的支付金额与订单总额不符！");
            }
            // 校验付款码
        }
        if (message.toString().length() > 0) {
            throw new PrivateException(500, message.toString());
        }
        for (int i = 0; i < list.size(); i++) {
            OrderExcelImportDTO item = list.get(i);
            orderPayment = new OrderPayment();
            orderPayment.setPayType(payType);
            orderPayment.setCreateTime(DateUtil.date());
            OrderInfo orderInfo = orderInfoMapper.selectOrderDetailInfoByOrderNo(orderNo);
            orderPayment.setOrderId(orderInfo.getId());
            orderPayment.setPayAmount(new BigDecimal(item.getSumAmount()));
            orderPayment.setPayNo(payNo);
            orderInfoMapper.updateOrderStatus(orderInfo.getId(), 3);
            orderPaymentMapper.insertOrderPayment(orderPayment);
        }
    }

    public static void main(String[] args) {
        BigDecimal sum = new BigDecimal("66");
        BigDecimal item = new BigDecimal("56");
        System.out.println(sum.add(new BigDecimal(10)).compareTo(item));
    }

/*    private void checkProductExpress(List<OrderDetail> orderDetails, List<OrderExpress> orderExpresses) {
        for () {
        }

    }*/
}
