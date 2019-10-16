package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class ProductPackageContentEntity extends EntityBase implements Serializable {

    // 产品ID
    private String productId;

    // 产品包ID
    private String productPackageId;

    /// 产品数量
    private Integer number;

    // dr
    private Boolean dr;

}
