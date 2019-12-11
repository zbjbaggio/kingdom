package com.kingdom.system.mapper;


import com.kingdom.system.data.entity.Category;

import java.util.List;

/**
 * 系统参数 数据层
 *
 * @author kingdom
 * @date 2019-12-11
 */
public interface CategoryMapper {
    /**
     * 查询系统参数信息
     *
     * @param id 系统参数ID
     * @return 系统参数信息
     */
    public Category selectCategoryById(Long id);

    /**
     * 查询系统参数列表
     *
     * @param category 系统参数信息
     * @return 系统参数集合
     */
    public List<Category> selectCategoryList(Category category);

    /**
     * 新增系统参数
     *
     * @param category 系统参数信息
     * @return 结果
     */
    public int insertCategory(Category category);

    /**
     * 修改系统参数
     *
     * @param category 系统参数信息
     * @return 结果
     */
    public int updateCategory(Category category);

    /**
     * 删除系统参数
     *
     * @param id 系统参数ID
     * @return 结果
     */
    public int deleteCategoryById(Long id);

    /**
     * 批量删除系统参数
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCategoryByIds(String[] ids);

}