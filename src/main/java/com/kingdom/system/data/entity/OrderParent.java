package com.kingdom.system.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kingdom.system.constant.TimeZoneConstant;
import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 截单主表 t_order_parent
 * 
 * @author kingdom
 * @date 2019-11-12
 */
@Data
@ToString(callSuper = true)
public class OrderParent extends EntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 截单名称 */
	@NotEmpty
	private String name;
	/** 截单人 */
	private Long operationId;
	/**  */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = TimeZoneConstant.DEFAULT)
	@NotNull
	private Date date;

	private Long orderUserId;

}
