package com.kingdom.system.mapper;


import com.kingdom.system.data.entity.OrderInfo;
import com.kingdom.system.data.entity.OrderParent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 截单主 数据层
 * 
 * @author kingdom
 * @date 2019-11-12
 */
public interface OrderParentMapper 
{
	/**
     * 查询截单主信息
     * 
     * @param id 截单主ID
     * @return 截单主信息
     */
	public OrderParent selectOrderParentById(Long id);
	
	/**
     * 查询截单主列表
     * 
     * @param orderParent 截单主信息
     * @return 截单主集合
     */
	public List<OrderParent> selectOrderParentList(OrderParent orderParent);
	
	/**
     * 新增截单主
     * 
     * @param orderParent 截单主信息
     * @return 结果
     */
	public int insertOrderParent(OrderParent orderParent);
	
	/**
     * 修改截单主
     * 
     * @param orderParent 截单主信息
     * @return 结果
     */
	public int updateOrderParent(OrderParent orderParent);
	
	/**
     * 删除截单主
     * 
     * @param id 截单主ID
     * @return 结果
     */
	public int deleteOrderParentById(Long id);
	
	/**
     * 批量删除截单主
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteOrderParentByIds(String[] ids);

    List<OrderParent> selectOrderParentListByName(@Param(value = "name") String name);

    OrderParent selectOrderParentByDate(@Param(value = "now")String now);
}