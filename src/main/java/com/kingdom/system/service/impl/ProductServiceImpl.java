package com.kingdom.system.service.impl;

import com.kingdom.system.data.dto.ProductDTO;
import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.Product;
import com.kingdom.system.data.entity.ProductPackage;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.ProductPackageVO;
import com.kingdom.system.data.vo.ProductVO;
import com.kingdom.system.mapper.ProductMapper;
import com.kingdom.system.mapper.ProductPackageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.*;

@Service
@Slf4j
public class ProductServiceImpl {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductPackageMapper productPackageMapper;

    public List<ProductVO> listProduct(String search, String sendDateStart, String sendDateEnd) {
        return productMapper.listProduct(search, sendDateStart, sendDateEnd);
    }

    public List<ProductVO> listProductPackage(String search) {
        List<ProductVO> productVOList = productMapper.listProductPackage(search);
        getPackage(productVOList);
        return productVOList;
    }

    private void getPackage(List<ProductVO> productVOList) {
        List<Long> ids = new ArrayList<>();
        for (ProductVO productVO : productVOList) {
            if (productVO.getType() == 1) {
                ids.add(productVO.getId());
            }
        }
        if (ids.size() > 0) {
            List<ProductPackageVO> productPackageList = productPackageMapper.listByProductIds(ids);
            Map<Long, List<ProductPackageVO>> map = new HashMap<>();
            for (ProductPackageVO productPackage : productPackageList) {
                List<ProductPackageVO> list = map.get(productPackage.getProductId());
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(productPackage);
            }
            for (ProductVO productVO : productVOList) {
                productVO.setProductPackageVOList(map.get(productVO.getId()));
            }
        }
    }

    public Product insert(Product product) {
        checkMemberNo(product);
        int count = productMapper.insertProduct(product);
        if (count != 1) {
            log.error("产品保存报错！product：{}", product);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        return product;
    }

    public Product update(Product product) {
        checkMemberNo(product);
        int count = productMapper.updateProduct(product);
        if (count != 1) {
            log.error("产品修改报错！product：{}", product);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        return product;
    }

    private void checkMemberNo(Product product) {
        int count = productMapper.listByCode(product);
        if (count > 0) {
            log.error("产品编号不唯一！ product：{}", product);
            throw new PrivateException(ErrorInfo.MEMBER_NO_ERROR);
        }
    }

    public void updateStatus(String id, int status) {
        int count = productMapper.updateStatus(id, status);
        if (count != 1) {
            log.error("产品修改状态失败！id：{}, status: {}", id, status);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
    }

    public void updatePriority(String id) {

    }

    public List<Product> listAll(String name) {
        return productMapper.listAll(name);
    }

    @Transactional
    public ProductDTO insertProductPackage(ProductDTO product) {
        Product productEntity = product.getProduct();
        checkMemberNo(productEntity);
        Date createTime = new Date();
        productEntity.setCreateTime(createTime);
        int count = productMapper.insertProduct(productEntity);
        if (count != 1) {
            log.error("产品包新增失败！ productEntity: {}", productEntity);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        Long productId = productEntity.getId();
        count = insertProductPackage(product, productId);
        if (count != product.getProductPackageList().size()) {
            log.error("产品包新增失败！productPackage: {}, prodctId: {}", product.getProductPackageList(), productEntity.getId());
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        return product;
    }

    @Transactional
    public ProductDTO updateProductPackage(ProductDTO product) {
        Product productEntity = product.getProduct();
        checkMemberNo(productEntity);
        int count = productMapper.updateProduct(productEntity);
        if (count != 1) {
            log.error("产品包修改失败！ productEntity: {}", productEntity);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        Long productId = productEntity.getId();
        productPackageMapper.deleteProductPackageByProductId(productId);
        count = insertProductPackage(product, productId);
        if (count != product.getProductPackageList().size()) {
            log.error("产品包修改失败！productPackage: {}, prodctId: {}", product.getProductPackageList(), productEntity.getId());
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        return product;
    }

    private int insertProductPackage(ProductDTO product, Long productId) {
        int count;
        Date createTime = new Date();
        List<ProductPackage> productPackageList = product.getProductPackageList();
        for (ProductPackage productPackage : productPackageList) {
            productPackage.setCreateTime(createTime);
            productPackage.setProductId(productId);
        }
        count = productPackageMapper.insertProductPackages(productPackageList);
        return count;
    }
}
