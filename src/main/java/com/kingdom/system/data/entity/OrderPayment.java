package com.kingdom.system.data.entity;

import com.kingdom.system.ann.AllowableValue;
import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

        
/**
 * 订单付款表 t_order_payment
 * 
 * @author kingdom
 * @date 2019-10-26
 */
@Data
@ToString(callSuper = true)
public class OrderPayment extends EntityBase implements Serializable {

	/** 支付方式 0 港币划卡 1 微信 2  支付宝 3 银行转账 */
	@AllowableValue(intValues = {0, 1, 2, 3}, groups = {Insert.class, Update.class})
	private Integer payType;

	/** 付款金额 */
	@Min(value = 0, groups = {Insert.class, Update.class})
	@NotNull
	private BigDecimal payAmount;

	/** 付款码 */
	//@NotEmpty(groups = Insert.class)
	private String payNo;


	/** 订单编号 */
	private Long orderId;

}
