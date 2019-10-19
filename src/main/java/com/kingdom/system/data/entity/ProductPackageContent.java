package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

    
/**
 * 产品包和产品关系表 t_product_package_content
 * 
 * @author kingdom
 * @date 2019-10-18
 */
@Data
@ToString(callSuper = true)
public class ProductPackageContent extends EntityBase implements Serializable {

	/** dr状态 */
	private int dr;

	/** 产品数量 */
	@Min(1)
	private int number;

	/** 产品ID */
	@NotNull
	private Long productId;

	/** 产品包ID */
	private Long productPackageId;

}
