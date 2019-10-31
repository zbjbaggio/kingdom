package com.kingdom.system.data.entity;

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
 * 订单购买产品表 t_order_product
 * 
 * @author kingdom
 * @date 2019-10-26
 */
@Data
@ToString(callSuper = true)
public class OrderProduct extends EntityBase implements Serializable {

	/** 订单主表id */
	private Long orderId;

	/** 订单用户表id */
	private Long orderUserId;

	/** 产品id */
	@NotNull(groups = {Insert.class, Update.class})
	private Long productId;
	/** 产品名称 */

	private String productName;
	/** 数量 */

	@Min(value = 1, groups = {Insert.class, Update.class})
	private Integer number;
	/** 产品销售港币单价 */
	private BigDecimal hkSellingPrice;
	/** 产品成本港币单价 */
	private BigDecimal hkCostPrice;
	/** 该产品港币销售总额 */
	private BigDecimal hkAmount;
	/** 该产品港币成本总额 */
	private BigDecimal hkCostAmount;
	/** 产品销售人民币单价 */
	private BigDecimal cnySellingPrice;
	/** 产品成本人民币单价 */
	private BigDecimal cnyCostPrice;
	/** 该产品人民币销售总额 */
	private BigDecimal cnyAmount;
	/** 该产品人民币成本总额 */
	private BigDecimal cnyCostAmount;
	/**  汇率*/
	private BigDecimal exchangeRate;
	/**  汇率id*/
	private Long exchangeRateId;

	private Integer score;

}
