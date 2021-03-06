package com.kingdom.system.data.vo;

import com.kingdom.system.data.entity.OrderDetail;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class OrderDetailVO extends OrderDetail {

    private String orderUserName;

    private Long orderUserId;

    private String orderUserMemberNo;

}
