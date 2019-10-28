package com.kingdom.system.data.vo;

import com.kingdom.system.data.entity.OrderDetail;
import com.kingdom.system.data.entity.OrderInfo;
import com.kingdom.system.data.entity.OrderPayment;
import com.kingdom.system.data.entity.OrderProduct;
import lombok.Data;

import java.util.List;

@Data
public class OrderVO {

    private OrderInfo orderInfo;

    private List<OrderProduct> orderProducts;

    private List<OrderPayment> orderPayments;

    private List<OrderDetail> orderDetails;

}
