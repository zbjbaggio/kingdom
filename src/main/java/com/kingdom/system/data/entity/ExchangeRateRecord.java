package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

        
/**
 * 汇率记录表 t_exchange_rate_record
 * 
 * @author kingdom
 * @date 2019-10-19
 */
@Data
@ToString(callSuper = true)
public class ExchangeRateRecord extends EntityBase implements Serializable {

	/** 状态 0 使用 1 禁用 */
	private Integer status;

	/** 汇率 */
	@NotNull(groups = Insert.class)
	@Min(value = 0, groups = Insert.class)
	private BigDecimal rate;

}
