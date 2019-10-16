package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class ProductPackageEntity extends EntityBase implements Serializable {

    // 产品包名称
    private String name;

    // 产品包编号
    private String code;

    // 销量
    private int saleNum ;

    // 备注
    private String remark;

    // 权重优先级，越大越优先
    private int priority = 0;

    private Boolean dr;

}
