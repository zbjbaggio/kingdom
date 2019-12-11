package com.kingdom.system.service.impl;

import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.Product;
import com.kingdom.system.data.entity.ProductRemark;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.mapper.NoProductMapper;
import com.kingdom.system.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class NoProductServiceImpl {

    @Autowired
    private NoProductMapper noProductMapper;

    @Autowired
    private ProductMapper productMapper;

    public List<ProductRemark> listNoProduct(String search) {
        return noProductMapper.listNoProduct(search);
    }

    @Transactional
    public ProductRemark insertNoProduct(ProductRemark productRemark) {
        setProductName(productRemark);
        int count = noProductMapper.insertNoProduct(productRemark);
        if (count != 1) {
            log.error("产品备注保存报错！product：{}", productRemark);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        return productRemark;
    }

    private void setProductName(ProductRemark productRemark) {
        Product product = productMapper.getProductById(productRemark.getProductId());
        if (product == null) {
            log.error("产品id未找到！ productId：{}", productRemark.getProductId());
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        productRemark.setProductName(product.getName());
        productRemark.setOlderNumber(product.getStock());
    }

    public ProductRemark updateNoProduct(ProductRemark productRemark) {
        setProductName(productRemark);
        int count = noProductMapper.updateNoProduct(productRemark);
        if (count != 1) {
            log.error("产品备注修改报错！product：{}", productRemark);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        return productRemark;
    }

    public void remove(Long[] ids) {
        noProductMapper.deleteNoProductByIds(ids);
    }
}
