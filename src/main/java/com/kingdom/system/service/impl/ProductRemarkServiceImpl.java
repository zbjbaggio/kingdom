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
    public void insertProductRemarks(List<ProductRemark> productRemarks) {
        setProductName(productRemarks);
        int count = productRemarkMapper.insertProductRemarks(productRemarks);
        if (count != productRemarks.size()) {
            log.error("产品入库保存报错！productRemarks：{}", productRemarks);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        //修改商品库存
        productRemarks.forEach(productRemark -> {
            int number = productMapper.updateStock(productRemark.getProductId(), productRemark.getNumber(), productRemark.getOlderNumber());
            if (number != 1) {
                log.error("修改库存失败！product：{}", productRemark);
                throw new PrivateException(ErrorInfo.SAVE_ERROR);
            }
        });
    }

    private void setProductName(List<ProductRemark> productRemarks) {
        Set<Long> productIds = new HashSet<>();
        for (ProductRemark productRemark : productRemarks) {
            productIds.add(productRemark.getProductId());
        }
        List<ProductVO> productVOList = productMapper.listProductByIds(productIds);
        if (productVOList == null || productVOList.size() != productIds.size()) {
            log.error("产品id未找全！ productIds：{}", productIds);
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        Map<Long, ProductVO> map = new HashMap<>();
        productVOList.forEach(productVO -> map.put(productVO.getId(), productVO));
        productRemarks.forEach(productRemark -> {
            productRemark.setProductName(map.get(productRemark.getProductId()).getName());
            productRemark.setOlderNumber(map.get(productRemark.getProductId()).getStock());
        });
    }

    /*public ProductRemark updateProductRemark(ProductRemark productRemark) {
        setProductName(productRemark);
        int count = productRemarkMapper.updateProductRemark(productRemark);
        if (count != 1) {
            log.error("产品备注修改报错！product：{}", productRemark);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        return productRemark;
    }*/

    public void remove(Long[] ids) {
        productRemarkMapper.deleteProductRemarkByIds(ids);
    }
}
