package com.kingdom.system.service.impl;

import com.kingdom.system.data.entity.Category;
import com.kingdom.system.mapper.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> list() {
        return categoryMapper.selectCategoryList(null);
    }

    public void update(Category category) {
        categoryMapper.updateCategory(category);
    }
}
