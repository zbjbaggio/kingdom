package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.NoProduct;
import com.kingdom.system.data.entity.ProductRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 商品库存备注 数据层
 * 
 * @author kingdom
 * @date 2019-12-02
 */
public interface NoProductMapper {
	/**
     * 查询商品库存备注信息
     * 
     * @param id 商品库存备注ID
     * @return 商品库存备注信息
     */
	public ProductRemark selectNoProductById(Long id);
	
	/**
     * 查询商品库存备注列表
     * 
     * @param productRemark 商品库存备注信息
     * @return 商品库存备注集合
     */
	public List<ProductRemark> selectNoProductList(NoProduct noProduct);
	
	/**
     * 新增商品库存备注
     * 
     * @param productRemark 商品库存备注信息
     * @return 结果
     */
	public int insertNoProduct(NoProduct noProduct);
	
	/**
     * 修改商品库存备注
     * 
     * @param productRemark 商品库存备注信息
     * @return 结果
     */
	public int updateNoProduct(NoProduct noProduct);
	
	/**
     * 删除商品库存备注
     * 
     * @param id 商品库存备注ID
     * @return 结果
     */
	public int deleteNoProductById(Long id);
	
	/**
     * 批量删除商品库存备注
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteNoProductByIds(Long[] ids);

	List<ProductRemark> listNoProduct(@Param(value = "search") String search);

	List<NoProduct> listNoProductByIds(@Param(value = "array") Set<Long> parentIds);

	NoProduct selectProductByParentId(@Param(value = "noProductParentId")Long noProductParentId,@Param(value = "noProductId") Long noProductId);

	int updateNumber(@Param(value = "number")Integer number, @Param(value = "noProductId")Long noProductId, @Param(value = "oldNumber")Integer oldNumber);

    int deleteNoProductByProductParentId(Long id);
}