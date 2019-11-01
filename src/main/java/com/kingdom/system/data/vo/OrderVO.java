package com.kingdom.system.data.vo;

import com.kingdom.system.data.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class OrderVO {

    private OrderInfo orderInfo;

    private List<OrderUser> orderProducts;

    private List<OrderPayment> orderPayments;

    private List<OrderUser> orderDetails;

    private List<OrderExpress> orderExpresses;

}
