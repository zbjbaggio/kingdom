package com.kingdom.system.data.vo;

import com.kingdom.system.data.entity.ProductPackage;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ProductPackageVO extends ProductPackage {

    private String name;

    private String code;

}
