package com.kingdom.system.service.impl;

import com.kingdom.system.data.dto.OrderDTO;
import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.*;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.ProductVO;
import com.kingdom.system.mapper.*;
import com.kingdom.system.util.BigDecimalUtils;
import com.kingdom.system.util.DateUtil;
import com.kingdom.system.util.NOUtils;
import com.kingdom.system.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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

        }
        orderProductMapper.insertOrderProducts(orderProducts);
    }

    private void setUserId(OrderProduct orderProduct) {
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
        Map<Long, ProductVO> map = new HashMap<>();
        for (ProductVO productVO : productVOList) {
            map.put(productVO.getId(), productVO);
        }
        return map;
    }

}
