package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.OrderProduct;
import com.kingdom.system.data.entity.ProductPackage;

import java.util.List;

/**
 * 订单购买产品 数据层
 *
 * @author kingdom
 * @date 2019-10-26
 */
public interface OrderProductMapper {
    /**
     * 查询订单购买产品信息
     *
     * @param id 订单购买产品ID
     * @return 订单购买产品信息
     */
    public OrderProduct selectOrderProductById(Long id);

    /**
     * 查询订单购买产品列表
     *
     * @param orderProduct 订单购买产品信息
     * @return 订单购买产品集合
     */
    public List<OrderProduct> selectOrderProductList(OrderProduct orderProduct);

    /**
     * 新增订单购买产品
     *
     * @param orderProduct 订单购买产品信息
     * @return 结果
     */
    public int insertOrderProduct(OrderProduct orderProduct);

    /**
     * 修改订单购买产品
     *
     * @param orderProduct 订单购买产品信息
     * @return 结果
     */
    public int updateOrderProduct(OrderProduct orderProduct);

    /**
     * 删除订单购买产品
     *
     * @param id 订单购买产品ID
     * @return 结果
     */
    public int deleteOrderProductById(Long id);

    /**
     * 批量删除订单购买产品
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderProductByIds(String[] ids);

    int insertOrderProducts(List<OrderProduct> orderProducts);

}