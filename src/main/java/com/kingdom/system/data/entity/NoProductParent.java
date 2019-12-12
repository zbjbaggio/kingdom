package com.kingdom.system.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kingdom.system.constant.TimeZoneConstant;
import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 欠货总表 t_no_product_parent
 * 
 * @author kingdom
 * @date 2019-12-12
 */
@Data
@ToString(callSuper = true)
public class NoProductParent extends EntityBase implements Serializable {
	/** 日期 */
	@NotNull(groups = BaseInfo.class)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = TimeZoneConstant.DEFAULT)
	private Date date;

	private String remark;

	@Valid
	@NotNull(groups = BaseInfo.class)
	@Size(min = 1)
	private List<NoProduct> noProductList;

	public interface BaseInfo {
	}
}
