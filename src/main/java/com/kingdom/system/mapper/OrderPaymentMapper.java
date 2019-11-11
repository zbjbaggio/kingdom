package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.OrderPayment;

import java.util.List;

/**
 * 订单付款 数据层
 *
 * @author kingdom
 * @date 2019-10-26
 */
public interface OrderPaymentMapper {
    /**
     * 查询订单付款信息
     *
     * @param id 订单付款ID
     * @return 订单付款信息
     */
    public OrderPayment selectOrderPaymentById(Long id);

    /**
     * 查询订单付款列表
     *
     * @param orderPayment 订单付款信息
     * @return 订单付款集合
     */
    public List<OrderPayment> selectOrderPaymentList(OrderPayment orderPayment);

    /**
     * 新增订单付款
     *
     * @param orderPayment 订单付款信息
     * @return 结果
     */
    public int insertOrderPayment(OrderPayment orderPayment);

    /**
     * 修改订单付款
     *
     * @param orderPayment 订单付款信息
     * @return 结果
     */
    public int updateOrderPayment(OrderPayment orderPayment);

    /**
     * 删除订单付款
     *
     * @param id 订单付款ID
     * @return 结果
     */
    public int deleteOrderPaymentById(Long id);

    /**
     * 批量删除订单付款
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderPaymentByIds(String[] ids);

    int insertOrderPayments(List<OrderPayment> orderPayments);

    List<OrderPayment> selectOrderPaymentListByOrderId(Long orderId);

    int deleteOrderPaymentByOrderId(Long orderId);

    List<OrderPayment> listPaymentByIds(List<Long> orderIds);
}