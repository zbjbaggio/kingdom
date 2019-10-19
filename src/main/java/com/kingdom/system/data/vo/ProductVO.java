package com.kingdom.system.data.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kingdom.system.data.entity.Product;
import com.kingdom.system.data.entity.ProductPackage;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ProductVO extends Product {

    private Long waitSendSum;

    private List<ProductPackageVO> productPackageVOList;
}
