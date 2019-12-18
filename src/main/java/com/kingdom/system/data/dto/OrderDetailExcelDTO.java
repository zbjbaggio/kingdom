package com.kingdom.system.data.dto;

import com.kingdom.system.util.excel.ExcelExport;
import lombok.Data;

@Data
public class OrderDetailExcelDTO {

    private Long orderId;

    @ExcelExport(name = "订货人会员号")
    private String userNo;

    @ExcelExport(name = "订货人姓名")
    private String username;

    @ExcelExport(name = "产品名称")
    private String productName;

    @ExcelExport(name = "数量")
    private Long number;

    @ExcelExport(name = "积分")
    private Long score;

}
