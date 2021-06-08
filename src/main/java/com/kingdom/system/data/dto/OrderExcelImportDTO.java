package com.kingdom.system.data.dto;

import com.kingdom.system.util.excel.ExcelImport;
import lombok.Data;

@Data
public class OrderExcelImportDTO {

    // 状态
    @ExcelImport
    private String status;

    //职员
    @ExcelImport
    private String user;

    //金额
    @ExcelImport
    private String amount;

    // 支付方式
    @ExcelImport
    private String payType;

    // 会员卡号
    @ExcelImport
    private String userNo;

    // 会员名称
    @ExcelImport
    private String username;

    // 自定贷款
    @ExcelImport
    private String privateAmount;

    // 运费
    @ExcelImport
    private String expressAmount;

    // 优惠券
    @ExcelImport
    private String coupon;

    // 商品运费
    @ExcelImport
    private String productExpress;

    // 总额
    @ExcelImport
    private String sumAmount;

    // 退款金额
    @ExcelImport
    private String returnAmount;

    // 二维码过期时间
    @ExcelImport
    private String codeEndTime;

    // 付款时间
    @ExcelImport
    private String payTime;

    // 交易id
    @ExcelImport
    private String payId;

    // 交易id2
    @ExcelImport
    private String payId2;

    // 创建时间
    @ExcelImport
    private String createTime;

    // 订单备注
    @ExcelImport
    private String desc;

}
