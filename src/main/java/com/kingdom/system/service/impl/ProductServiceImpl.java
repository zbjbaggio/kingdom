package com.kingdom.system.service.impl;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProductServiceImpl {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductPackageMapper productPackageMapper;

    public List<ProductVO> list(String search, String sendDateStart, String sendDateEnd) {
        List<ProductVO> productVOList = productMapper.list(search, sendDateStart, sendDateEnd);
        List<Long> ids = new ArrayList<>();
        for (ProductVO productVO : productVOList) {
            if (productVO.getType() == 1) {
                ids.add(productVO.getId());
            }
        }
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
        return productVOList;
    }

    public Product insert(Product product) {
        int count = productMapper.insertProduct(product);
        if (count != 1) {
            log.error("产品保存报错！product：{}", product);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        return product;
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
}
