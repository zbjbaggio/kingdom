package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.OrderExpressDetail;

import java.util.List;	

/**
 * 订单快递明细 数据层
 * 
 * @author kingdom
 * @date 2019-10-29
 */
public interface OrderExpressDetailMapper 
{
	/**
     * 查询订单快递明细信息
     * 
     * @param id 订单快递明细ID
     * @return 订单快递明细信息
     */
	public OrderExpressDetail selectOrderExpressDetailById(Long id);
	
	/**
     * 查询订单快递明细列表
     * 
     * @param orderExpressDetail 订单快递明细信息
     * @return 订单快递明细集合
     */
	public List<OrderExpressDetail> selectOrderExpressDetailList(OrderExpressDetail orderExpressDetail);
	
	/**
     * 新增订单快递明细
     * 
     * @param orderExpressDetail 订单快递明细信息
     * @return 结果
     */
	public int insertOrderExpressDetail(OrderExpressDetail orderExpressDetail);
	
	/**
     * 修改订单快递明细
     * 
     * @param orderExpressDetail 订单快递明细信息
     * @return 结果
     */
	public int updateOrderExpressDetail(OrderExpressDetail orderExpressDetail);
	
	/**
     * 删除订单快递明细
     * 
     * @param id 订单快递明细ID
     * @return 结果
     */
	public int deleteOrderExpressDetailById(Long id);
	
	/**
     * 批量删除订单快递明细
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteOrderExpressDetailByIds(String[] ids);

	List<OrderExpressDetail> selectOrderExpressDetailListByOrderId(Long orderId);

	int insertOrderExpressDetails(List<OrderExpressDetail> orderExpressDetailList);

	List<OrderExpressDetail> selectOrderExpressDetailListByExpressId(Long orderExpressId);

	int deleteOrderExpressDetailByExpressId(Long orderExpressId);
}