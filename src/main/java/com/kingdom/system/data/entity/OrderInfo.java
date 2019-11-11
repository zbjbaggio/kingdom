package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import com.kingdom.system.data.vo.OrderDetailVO;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


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
	@NotEmpty(groups = {Insert.class, Update.class})
	private String orderUsername;

	/** 下单人电话 */
	@NotEmpty(groups = {Insert.class, Update.class})
	private String orderPhone;

	/** 下单日期 */
	private Date date;

	private String userId;

	private String remark;

	/**
	 * 是否有快递 0 没有 1 有
	 */
	private Integer express;

	private Integer score;

	private BigDecimal cnyAmount;

	private BigDecimal hkAmount;

	private List<OrderDetailVO> orderDetailVOS;

	private List<OrderPayment> orderPayments;

}
