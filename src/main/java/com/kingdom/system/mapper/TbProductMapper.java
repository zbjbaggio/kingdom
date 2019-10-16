package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.ProductInfo;

import java.util.List;

/**
 * 产品 数据层
 *
 * @author kingdom
 * @date 2019-10-16
 */
public interface TbProductMapper {
    /**
     * 查询产品信息
     *
     * @param id 产品ID
     * @return 产品信息
     */
    public ProductInfo selectTbProductById(String id);

    /**
     * 查询产品列表
     *
     * @param tbProductInfo 产品信息
     * @return 产品集合
     */
    public List<ProductInfo> selectTbProductList(ProductInfo tbProductInfo);

    /**
     * 新增产品
     *
     * @param tbProductInfo 产品信息
     * @return 结果
     */
    public int insertTbProduct(ProductInfo tbProductInfo);

    /**
     * 修改产品
     *
     * @param tbProductInfo 产品信息
     * @return 结果
     */
    public int updateTbProduct(ProductInfo tbProductInfo);

    /**
     * 删除产品
     *
     * @param id 产品ID
     * @return 结果
     */
    public int deleteTbProductById(String id);

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTbProductByIds(String[] ids);

}