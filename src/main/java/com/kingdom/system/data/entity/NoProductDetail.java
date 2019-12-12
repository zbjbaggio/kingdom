package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 欠货返货流水表 t_no_product_detail
 * 
 * @author kingdom
 * @date 2019-12-12
 */
@Data
@ToString(callSuper = true)
public class NoProductDetail extends EntityBase implements Serializable {
	private static final long serialVersionUID = 1L;
	/**  */
	private Long productId;
	/**  */
	@NotNull(groups = BaseInfo.class)
	private Integer number;
	/**  */
	@NotNull(groups = BaseInfo.class)
	private Long noProductParentId;
	/**  */
	@NotNull(groups =BaseInfo.class)
	private Long noProductId;

	private String remark;

	private String productName;

	public interface BaseInfo {
	}


}
