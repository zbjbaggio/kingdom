package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.OrderExpress;

import java.util.List;

/**
 * 订单快递 数据层
 * 
 * @author kingdom
 * @date 2019-10-29
 */
public interface OrderExpressMapper 
{
	/**
     * 查询订单快递信息
     * 
     * @param id 订单快递ID
     * @return 订单快递信息
     */
	public OrderExpress selectOrderExpressById(Long id);
	
	/**
     * 查询订单快递列表
     * 
     * @param orderExpress 订单快递信息
     * @return 订单快递集合
     */
	public List<OrderExpress> selectOrderExpressList(OrderExpress orderExpress);
	
	/**
     * 新增订单快递
     * 
     * @param orderExpress 订单快递信息
     * @return 结果
     */
	public int insertOrderExpress(OrderExpress orderExpress);
	
	/**
     * 修改订单快递
     * 
     * @param orderExpress 订单快递信息
     * @return 结果
     */
	public int updateOrderExpress(OrderExpress orderExpress);
	
	/**
     * 删除订单快递
     * 
     * @param id 订单快递ID
     * @return 结果
     */
	public int deleteOrderExpressById(Long id);
	
	/**
     * 批量删除订单快递
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteOrderExpressByIds(String[] ids);

	List<OrderExpress> selectOrderExpressListByOrderId(Long orderId);
}