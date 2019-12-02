package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.ProductRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存备注 数据层
 * 
 * @author kingdom
 * @date 2019-12-02
 */
public interface ProductRemarkMapper 
{
	/**
     * 查询商品库存备注信息
     * 
     * @param id 商品库存备注ID
     * @return 商品库存备注信息
     */
	public ProductRemark selectProductRemarkById(Long id);
	
	/**
     * 查询商品库存备注列表
     * 
     * @param productRemark 商品库存备注信息
     * @return 商品库存备注集合
     */
	public List<ProductRemark> selectProductRemarkList(ProductRemark productRemark);
	
	/**
     * 新增商品库存备注
     * 
     * @param productRemark 商品库存备注信息
     * @return 结果
     */
	public int insertProductRemark(ProductRemark productRemark);
	
	/**
     * 修改商品库存备注
     * 
     * @param productRemark 商品库存备注信息
     * @return 结果
     */
	public int updateProductRemark(ProductRemark productRemark);
	
	/**
     * 删除商品库存备注
     * 
     * @param id 商品库存备注ID
     * @return 结果
     */
	public int deleteProductRemarkById(Long id);
	
	/**
     * 批量删除商品库存备注
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteProductRemarkByIds(Long[] ids);

	List<ProductRemark> listProductRemark(@Param(value = "search") String search);
}