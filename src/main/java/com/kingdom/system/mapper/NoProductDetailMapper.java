package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.NoProductDetail;

import java.util.List;

/**
 * 欠货返货流水 数据层
 * 
 * @author kingdom
 * @date 2019-12-12
 */
public interface NoProductDetailMapper 
{
	/**
     * 查询欠货返货流水信息
     * 
     * @param id 欠货返货流水ID
     * @return 欠货返货流水信息
     */
	public NoProductDetail selectNoProductDetailById(Long id);
	
	/**
     * 查询欠货返货流水列表
     * 
     * @param noProductDetail 欠货返货流水信息
     * @return 欠货返货流水集合
     */
	public List<NoProductDetail> selectNoProductDetailList(NoProductDetail noProductDetail);
	
	/**
     * 新增欠货返货流水
     * 
     * @param noProductDetail 欠货返货流水信息
     * @return 结果
     */
	public int insertNoProductDetail(NoProductDetail noProductDetail);
	
	/**
     * 修改欠货返货流水
     * 
     * @param noProductDetail 欠货返货流水信息
     * @return 结果
     */
	public int updateNoProductDetail(NoProductDetail noProductDetail);
	
	/**
     * 删除欠货返货流水
     * 
     * @param id 欠货返货流水ID
     * @return 结果
     */
	public int deleteNoProductDetailById(Long id);
	
	/**
     * 批量删除欠货返货流水
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteNoProductDetailByIds(String[] ids);

	List<NoProductDetail> selectNoProductDetailByNoProductId(Long noProductId);

    int deleteNoProductDetailByProductParentId(Long id);
}