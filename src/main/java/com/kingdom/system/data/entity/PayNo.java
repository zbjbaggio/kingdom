package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class PayNo extends EntityBase implements Serializable {

    private Long orderId;

    private String payNo;

}
