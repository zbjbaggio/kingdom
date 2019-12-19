package com.kingdom.system.data.dto;

import com.kingdom.system.data.entity.OrderPayment;
import com.kingdom.system.util.excel.ExcelExport;
import lombok.Data;

import java.util.List;

@Data
public class OrderExcelPayDTO {

    private Long id;

    private Long orderId;

    @ExcelExport(name = "订单编号")
    private String orderNo;

    @ExcelExport(name = "付款会员号")
    private String memberNo;

    @ExcelExport(name = "付款人姓名")
    private String orderUsername;

    @ExcelExport
    List<OrderPayment> orderPayments;

}
