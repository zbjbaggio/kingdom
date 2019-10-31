package com.kingdom.system.data.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrderUserVO {

    private String orderUserName;

    private Long orderUserId;

    private String orderUserMemberNo;

    private List<OrderDetailVO> orderDetailVOList;
}
