package com.kingdom.system.mapper;


import com.kingdom.system.data.entity.OrderUser;
import com.kingdom.system.data.vo.OrderDetailVO;

import java.util.List;

/**
 * 订单下单人 数据层
 * 
 * @author kingdom
 * @date 2019-10-31
 */
public interface OrderUserMapper 
{
	/**
     * 查询订单下单人信息
     * 
     * @param id 订单下单人ID
     * @return 订单下单人信息
     */
	public OrderUser selectOrderUserById(Long id);
	
	/**
     * 查询订单下单人列表
     * 
     * @param orderUser 订单下单人信息
     * @return 订单下单人集合
     */
	public List<OrderUser> selectOrderUserList(OrderUser orderUser);
	
	/**
     * 新增订单下单人
     * 
     * @param orderUser 订单下单人信息
     * @return 结果
     */
	public int insertOrderUser(OrderUser orderUser);
	
	/**
     * 修改订单下单人
     * 
     * @param orderUser 订单下单人信息
     * @return 结果
     */
	public int updateOrderUser(OrderUser orderUser);
	
	/**
     * 删除订单下单人
     * 
     * @param id 订单下单人ID
     * @return 结果
     */
	public int deleteOrderUserById(Long id);
	
	/**
     * 批量删除订单下单人
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteOrderUserByIds(String[] ids);

	List<OrderUser> selectOrderUserListByOrderId(Long orderId);
}