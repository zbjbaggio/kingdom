package com.kingdom.system.service.impl;

import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.Product;
import com.kingdom.system.data.entity.ProductRemark;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.ProductVO;
import com.kingdom.system.mapper.ProductMapper;
import com.kingdom.system.mapper.ProductRemarkMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class ProductRemarkServiceImpl {

    @Autowired
    private ProductRemarkMapper productRemarkMapper;

    @Autowired
    private ProductMapper productMapper;

    public List<ProductRemark> listProductRemark(String search) {
        return productRemarkMapper.listProductRemark(search);
    }

    @Transactional
    public ProductRemark insertProductRemark(ProductRemark productRemark) {
        setProductName(productRemark);
        int count = productRemarkMapper.insertProductRemark(productRemark);
        if (count != 1) {
            log.error("产品备注保存报错！product：{}", productRemark);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        //修改商品库存
        count = productMapper.updateStock(productRemark.getProductId(), productRemark.getNumber(), productRemark.getOlderNumber());
        if (count != 1) {
            log.error("修改库存失败！product：{}", productRemark);
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

    public ProductRemark updateProductRemark(ProductRemark productRemark) {
        setProductName(productRemark);
        int count = productRemarkMapper.updateProductRemark(productRemark);
        if (count != 1) {
            log.error("产品备注修改报错！product：{}", productRemark);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        return productRemark;
    }

    public void remove(Long[] ids) {
        productRemarkMapper.deleteProductRemarkByIds(ids);
    }
}
