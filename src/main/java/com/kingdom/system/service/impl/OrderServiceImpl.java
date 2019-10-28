package com.kingdom.system.service.impl;

import com.kingdom.system.data.dto.OrderDTO;
import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.*;
import com.kingdom.system.data.exception.PrivateException;
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

    @Transactional

    public OrderDTO insert(OrderDTO orderDTO) {
        Map<Long, ProductVO> productNameMap = checkOrder(orderDTO);
        OrderInfo orderInfo = orderDTO.getOrderInfo();
        orderInfo.setStatus(1);
        orderInfo.setOrderNo(NOUtils.getGeneratorNO());
        orderInfo.setDate(DateUtil.date());
        int count = orderInfoMapper.insertOrderInfo(orderInfo);
        if (count != 1) {
            log.error("订单保存报错！orderDTO：{}", orderDTO);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        saveOrderProduct(orderDTO.getOrderProducts(), orderInfo.getId(), productNameMap);
        List<OrderPayment> orderPayments = orderDTO.getOrderPayments();
        for (OrderPayment orderPayment : orderPayments) {
            orderPayment.setOrderId(orderInfo.getId());
        }
        orderPaymentMapper.insertOrderPayments(orderPayments);
        return orderDTO;
    }

    private void saveOrderProduct(List<OrderProduct> orderProducts, Long orderId, Map<Long, ProductVO> productMap) {
        ExchangeRateRecord exchangeRateRecord = exchangeRateRecordMapper.selectDefault();
        BigDecimal rate = exchangeRateRecord.getRate();
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail = null;
        for (OrderProduct orderProduct : orderProducts) {
            setUserId(orderProduct);
            orderProduct.setOrderId(orderId);
            ProductVO productVO = productMap.get(orderProduct.getProductId());
            orderProduct.setProductName(productVO.getName());
            orderProduct.setExchangeRate(rate);
            orderProduct.setExchangeRateId(exchangeRateRecord.getId());
            orderProduct.setHkCostPrice(productVO.getCostPrice());
            orderProduct.setHkSellingPrice(productVO.getSellingPrice());
            orderProduct.setHkCostAmount(BigDecimalUtils.multiply(productVO.getSellingPrice(), orderProduct.getNumber()));
            orderProduct.setHkAmount(BigDecimalUtils.multiply(productVO.getCostPrice(), orderProduct.getNumber()));
            orderProduct.setCnyCostPrice(productVO.getCostPrice().multiply(rate));
            orderProduct.setCnySellingPrice(productVO.getSellingPrice().multiply(rate));
            orderProduct.setCnyCostAmount(BigDecimalUtils.multiply(productVO.getCostPrice(), orderProduct.getNumber()).multiply(rate));
            orderProduct.setCnyAmount(BigDecimalUtils.multiply(productVO.getSellingPrice(), orderProduct.getNumber()).multiply(rate));
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
                orderDetails.add(orderDetail);
            }
            //orderProduct.getp
        }
        orderDetailMapper.insertOrderDetails(orderDetails);
        //orderProductMapper.insertOrderProducts(orderProducts);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setUserId(OrderProduct orderProduct) {
        if (StringUtils.isNoneEmpty(orderProduct.getOrderUserMemberNo())) {
            UserEntity userEntity = userMapper.selectUserByMemberNo(orderProduct.getOrderUserMemberNo());
            if (userEntity == null) {
                userEntity = new UserEntity();
                userEntity.setRealName(orderProduct.getOrderUserName());
                userEntity.setMemberNo(orderProduct.getOrderUserMemberNo());
                userMapper.insertUser(userEntity);
            }
            orderProduct.setOrderUserId(userEntity.getId());
        }
    }

    //返回产品名称map
    private Map<Long, ProductVO> checkOrder(OrderDTO orderDTO) {
        List<OrderProduct> list = orderDTO.getOrderProducts();
        Set<Long> productIds = new HashSet<>();
        for (OrderProduct orderProduct : list) {
            productIds.add(orderProduct.getProductId());
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
        orderVO.setOrderDetails(orderDetailMapper.selectOrderDetailListByOrderId(orderId));
        orderVO.setOrderProducts(orderProductMapper.selectOrderProductListByOrderId(orderId));
        orderVO.setOrderPayments(orderPaymentMapper.selectOrderPaymentListByOrderId(orderId));
        return orderVO;
    }
}
