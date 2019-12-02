package com.kingdom.system.service.impl;

import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.ProductRemark;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.mapper.ProductRemarkMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductRemarkServiceImpl {

    @Autowired
    private ProductRemarkMapper productRemarkMapper;

    public List<ProductRemark> listProductRemark(String search) {
        return productRemarkMapper.listProductRemark(search);
    }

    public ProductRemark insertProductRemark(ProductRemark productRemark) {
        int count = productRemarkMapper.insertProductRemark(productRemark);
        if (count != 1) {
            log.error("产品备注保存报错！product：{}", productRemark);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        return productRemark;
    }

    public ProductRemark updateProductRemark(ProductRemark productRemark) {
        int count = productRemarkMapper.updateProductRemark(productRemark);
        if (count != 1) {
            log.error("产品备注修改报错！product：{}", productRemark);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        return productRemark;
    }
}
