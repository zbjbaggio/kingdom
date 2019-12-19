package com.kingdom.system.service.impl;

import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.*;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.mapper.NoProductDetailMapper;
import com.kingdom.system.mapper.NoProductMapper;
import com.kingdom.system.mapper.NoProductParentMapper;
import com.kingdom.system.mapper.ProductMapper;
import com.kingdom.system.util.DateUtil;
import com.kingdom.system.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class NoProductServiceImpl {

    @Autowired
    private NoProductMapper noProductMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private NoProductParentMapper noProductParentMapper;

    @Autowired
    private NoProductDetailMapper noProductDetailMapper;

    public List<NoProductParent> listNoProduct(String date) {
        NoProductParent noProductParent = new NoProductParent();
        if (StringUtils.isNoneEmpty(date)) {
            noProductParent.setDate(DateUtil.formatDate(date));
        }
        List<NoProductParent> noProductParents = noProductParentMapper.selectNoProductParentList(noProductParent);
        if (noProductParents != null && noProductParents.size() > 0) {
            Set<Long> ids = new HashSet<>(noProductParents.size());
            noProductParents.forEach(productParent -> ids.add(productParent.getId()));
            List<NoProduct> noProducts = noProductMapper.listNoProductByIds(ids);
            Map<Long, List<NoProduct>> map = new HashMap<>();
            noProducts.forEach(norProduct -> {
                List<NoProduct> noProductList = map.get(norProduct.getNoProductParentId());
                if (noProductList == null) {
                    noProductList = new ArrayList<>();
                }
                noProductList.add(norProduct);
                map.put(norProduct.getNoProductParentId(), noProductList);
            });
            noProductParents.forEach(productParent -> productParent.setNoProductList(map.get(productParent.getId())));
        }
        return noProductParents;
    }

    @Transactional
    public void insertNoProduct(NoProductParent noProductParent) {
        int count = noProductParentMapper.insertNoProductParent(noProductParent);
        if (count != 1) {
            log.error("欠货报错失败！product：{}", noProductParent);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        saveProduct(noProductParent);
    }

    private void saveProduct(NoProductParent noProductParent) {
        List<NoProduct> noProducts = noProductParent.getNoProductList();
        noProducts.forEach(noProduct -> {
            Product product = productMapper.getProductById(noProduct.getProductId());
            if (product == null) {
                log.error("产品id未找到！ productId：{}", noProduct.getProductId());
                throw new PrivateException(ErrorInfo.PARAMS_ERROR);
            }
            noProduct.setProductName(product.getName());
            noProduct.setNoProductParentId(noProductParent.getId());
            noProduct.setOlderNumber(product.getStock());
            noProductMapper.insertNoProduct(noProduct);
        });
    }

  /*  public ProductRemark updateNoProduct(ProductRemark productRemark) {
        //setProductName(productRemark);
        int count = noProductMapper.updateNoProduct(productRemark);
        if (count != 1) {
            log.error("产品备注修改报错！product：{}", productRemark);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        return productRemark;
    }*/

    @Transactional
    public void remove(Long id) {
        noProductParentMapper.deleteNoProductParentById(id);
        noProductMapper.deleteNoProductByProductParentId(id);
        noProductDetailMapper.deleteNoProductDetailByProductParentId(id);
    }

    @Transactional
    public void insertNoProductDetail(NoProductDetail noProductDetail) {
        NoProduct noProduct = noProductMapper.selectProductByParentId(noProductDetail.getNoProductParentId(), noProductDetail.getNoProductId());
        if (noProduct == null) {
            log.error("欠货流水保存失败！ noProductDetail：{}", noProductDetail);
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        noProductDetail.setProductId(noProduct.getProductId());
        noProductDetail.setProductName(noProduct.getProductName());
        noProductDetailMapper.insertNoProductDetail(noProductDetail);
        int count = noProductMapper.updateNumber(noProductDetail.getNumber(), noProductDetail.getNoProductId(), noProduct.getNumber());
        if (count != 1) {
            log.error("修改欠货流水失败！");
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
    }

    public List<NoProductDetail> listDetail(Long noProductId) {
        return noProductDetailMapper.selectNoProductDetailByNoProductId(noProductId);
    }
}
