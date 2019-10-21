package com.kingdom.system.data.dto;

import com.kingdom.system.data.entity.Product;
import com.kingdom.system.data.entity.ProductPackage;
import com.kingdom.system.data.entity.ProductPackageContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by lilong on 2018/6/15.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1015716591405659486L;

    @NotNull(groups = {Product.Insert.class, Product.Update.class})
    @Valid
    private Product product;

    @NotNull(groups = {Product.Insert.class, Product.Update.class})
    @Size(min = 1, groups = {Product.Insert.class, Product.Update.class})
    @Valid
    private List<ProductPackage> productPackageList;

}
