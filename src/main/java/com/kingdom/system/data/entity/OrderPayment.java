package com.kingdom.system.data.entity;

import com.kingdom.system.ann.AllowableValue;
import com.kingdom.system.data.base.EntityBase;
import com.kingdom.system.data.dto.OrderDTO;
import com.kingdom.system.util.excel.ExcelExport;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单付款表 t_order_payment
 *
 * @author kingdom
 * @date 2019-10-26
 */
@Data
@ToString(callSuper = true)
public class OrderPayment extends EntityBase implements Serializable {

	@ExcelExport(name = "付款方式")
	private String payTypeString;

	/** 付款码 */
	@ExcelExport(name = "付款码")
	private String payNo;

	/** 支付方式 0 港币划卡 1 微信 2  支付宝 3 银行转账 */
	@AllowableValue(intValues = {0, 1, 2, 3}, groups = {Insert.class, OrderDTO.BASE.class})
	private Integer payType;

	/** 付款金额 */
	@ExcelExport(name = "付款金额")
	@Min(value = 0, groups = {Insert.class, OrderDTO.BASE.class})
	//@NotNull(groups = {Insert.class, OrderDTO.BASE.class})
	private BigDecimal payAmount;

	/** 订单编号 */
	private Long orderId;



}
