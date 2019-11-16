package com.kingdom.system.mapper;


import com.kingdom.system.data.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单 数据层
 *
 * @author kingdom
 * @date 2019-10-25
 */
public interface OrderInfoMapper {
    /**
     * 查询订单信息
     *
     * @param id 订单ID
     * @return 订单信息
     */
    public OrderInfo selectOrderInfoById(Long id);

    OrderInfo selectOrderDetailInfoById(Long id);

    /**
     * 查询订单列表
     *
     * @param orderInfo 订单信息
     * @return 订单集合
     */
    public List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo);

    public List<OrderInfo> selectOrderInfoList(@Param(value = "payUser") String payUser, @Param(value = "orderUser") String orderUser,
                                               @Param(value = "express") String express, @Param(value = "startDate") String startDate,
                                               @Param(value = "endDate") String endDate);

    /**
     * 新增订单
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    public int insertOrderInfo(OrderInfo orderInfo);

    /**
     * 修改订单
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    public int updateOrderInfo(OrderInfo orderInfo);

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return 结果
     */
    public int deleteOrderInfoById(Long id);

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderInfoByIds(String[] ids);

    int updateDetail(@Param(value = "id") Long id, @Param(value = "remark") String remark);

    int updateOrderParentId(@Param(value = "orderParentId")Long orderParentId);
}