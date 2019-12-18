package com.kingdom.system.data.dto;

import com.kingdom.system.util.excel.ExcelExport;
import lombok.Data;

import java.util.List;

@Data
public class OrderExcelDTO {

    private Long id;

    @ExcelExport(name = "订单编号")
    private String orderNo;

    @ExcelExport(name = "会员号")
    private String memberNo;

    @ExcelExport(name = "会员姓名")
    private String orderUsername;

    @ExcelExport(name = "状态")
    private String statusString;

    @ExcelExport(name = "备注")
    private String remark;

    private List<OrderDetailExcelDTO> orderDetailExcelDTOList;

}
