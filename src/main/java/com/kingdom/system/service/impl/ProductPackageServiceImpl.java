package com.kingdom.system.service.impl;

import com.kingdom.system.data.dto.ProductDTO;
import com.kingdom.system.mapper.ProductPackageMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class ProductPackageServiceImpl {

    @Inject
    private ProductPackageMapper productPackageMapper;


    public ProductDTO insert(ProductDTO productDTO) {
        return null;
    }

    public List<?> list(String s) {
        return null;
    }
}
