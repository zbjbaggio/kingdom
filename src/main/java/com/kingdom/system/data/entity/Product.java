package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

        
/**
 * 产品表 t_product
 * 
 * @author kingdom
 * @date 2019-10-19
 */
@Data
@ToString(callSuper = true)
public class Product extends EntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 产品编号 */
	private String code;
	/** 产品名称 */
	private String name;
	/** 状态 0 下架  1 上架 */
	private Integer status;
	/** 库存 */
	private Integer stock;
	/** 总销量 */
	private Integer totalSale;
	/** 权重优先级，越大越优先 */
	private Integer priority;
	/** 入库总量 */
	private Integer totalIn;
	/**成本价格 港币*/
	private BigDecimal costPrice;
	/**销售价格 港币 */
	private BigDecimal sellingPrice;
	/**积分 */
	private Integer score;

	/**
	 * 类型  0 单个产品  1 产品包
	 */
	private int type;

	private String remark;

}
