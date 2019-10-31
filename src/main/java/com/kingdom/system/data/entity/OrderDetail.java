package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

    
/**
 * 订单单品详情表 t_order_detail
 * 
 * @author kingdom
 * @date 2019-10-28
 */
@Data
@ToString(callSuper = true)
public class OrderDetail extends EntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 订单主表id */
	private Long orderId;
	/** 订单购买产品表id */
	private Long orderProductId;

	/** 订单购买产品表id */
	private Long orderUserId;
	/** 单品id */
	private Long productId;

	private String productName;

	/** 单品总数 */
	private Integer number;

	/** 发货数量*/
	private Integer expressNumber;

	/** 单品对应的产品包id，可能为空 */
	private Long productPackageId;

}
