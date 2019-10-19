package com.kingdom.system.service.impl;

import com.kingdom.system.data.dto.ProductPackageInfoDTO;
import com.kingdom.system.mapper.ProductPackageMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class ProductPackageServiceImpl {

    @Inject
    private ProductPackageMapper productPackageMapper;


    public ProductPackageInfoDTO insert(ProductPackageInfoDTO productPackageInfoDTO) {
        return null;
    }

    public List<?> list(String s) {
        return null;
    }
}
