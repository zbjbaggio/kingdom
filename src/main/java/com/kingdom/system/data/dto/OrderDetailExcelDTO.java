package com.kingdom.system.data.dto;

import lombok.Data;

@Data
public class OrderDetailExcelDTO {

    private Long orderId;

    private String userNo;

    private String username;

    private String productName;

    private Long number;

    private Long score;

}
