package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;


/**
 * 订单表 t_order_info
 * 
 * @author kingdom
 * @date 2019-10-25
 */
@Data
@ToString(callSuper = true)
public class OrderInfo extends EntityBase implements Serializable {

	/** 订单状态 1 正常订单 2 支付中订单 3 订单支付完成 4 订单处理完成 */
	private Integer status;

	/** 订单号 */
	private String orderNo;

	/**  */
	private Integer dr;

	/** 截单主表id */
	private Long parentOrderId;

	/** 下单人姓名 */
	@NotEmpty(groups = Insert.class)
	private String orderUsername;

	/** 下单人电话 */
	@NotEmpty(groups = Insert.class)
	private String orderPhone;

	/** 下单日期 */
	private Date date;

	private String userId;

	private String remark;

}
