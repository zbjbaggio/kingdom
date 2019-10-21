package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
	@NotNull(groups = {Update.class})
	private Long productId;

	/** 产品包中包含的产品id */
	@NotNull(groups = {Insert.class, Update.class})
	private Long productBId;

	/** 数量 */
	@NotNull(groups = {Insert.class, Update.class})
	@Min(value = 1, groups = {Insert.class, Update.class})
	private Integer number;

}
