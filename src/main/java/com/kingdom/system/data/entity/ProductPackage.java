package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * 产品包表 t_product_package
 * 
 * @author kingdom
 * @date 2019-10-19
 */
@Data
@ToString(callSuper = true)
public class ProductPackage extends EntityBase implements Serializable {

	/** 产品id */
	private Long productId;

	/** 产品包中包含的产品id */
	private Long productBId;

	/** 数量 */
	private Integer number;

}
