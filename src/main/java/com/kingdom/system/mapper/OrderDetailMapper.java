package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.OrderDetail;
import com.kingdom.system.data.vo.OrderDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单单品详情 数据层
 *
 * @author kingdom
 * @date 2019-10-28
 */
public interface OrderDetailMapper {
    /**
     * 查询订单单品详情信息
     *
     * @param id 订单单品详情ID
     * @return 订单单品详情信息
     */
    public OrderDetail selectOrderDetailById(Long id);

    /**
     * 查询订单单品详情列表
     *
     * @param orderDetail 订单单品详情信息
     * @return 订单单品详情集合
     */
    public List<OrderDetail> selectOrderDetailList(OrderDetail orderDetail);

    /**
     * 新增订单单品详情
     *
     * @param orderDetail 订单单品详情信息
     * @return 结果
     */
    public int insertOrderDetail(OrderDetail orderDetail);

    /**
     * 修改订单单品详情
     *
     * @param orderDetail 订单单品详情信息
     * @return 结果
     */
    public int updateOrderDetail(OrderDetail orderDetail);

    /**
     * 删除订单单品详情
     *
     * @param id 订单单品详情ID
     * @return 结果
     */
    public int deleteOrderDetailById(Long id);

    /**
     * 批量删除订单单品详情
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderDetailByIds(String[] ids);

    int insertOrderDetails(List<OrderDetail> orderDetails);

    List<OrderDetailVO> selectOrderDetailListByOrderId(Long orderId);

    List<OrderDetailVO> selectOrderDetailPackageListByOrderId(Long orderId);

    int deleteOrderDetailByOrderId(Long orderId);

    List<OrderDetailVO> selectExpressByOrderIdAndUserId(@Param(value = "orderId") Long orderId, @Param(value = "orderUserId") Long orderUserId);

    int updateNumber(@Param(value = "id") Long id, @Param(value = "expressNumber") int expressNumber);

    List<OrderDetailVO> selectExpressByOrderIdExpress(@Param(value = "orderId") Long orderId, @Param(value = "orderUserId") Long orderUserId);
}