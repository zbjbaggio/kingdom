package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * 订单快递表 t_order_express
 * 
 * @author kingdom
 * @date 2019-10-29
 */
@Data
@ToString(callSuper = true)
public class OrderExpress extends EntityBase implements Serializable {
	/** 订单主表id */
	@NotNull(groups = {Insert.class, Update.class})
	private Long orderId;

	/** 订货人id */
	@NotNull(groups = {Insert.class, Update.class})
	private Long orderUserId;

	/** 收货人姓名 */
	@NotEmpty(groups = {Insert.class, Update.class})
	private String expressName;

	/** 收货地址 */
	@NotEmpty(groups = {Insert.class, Update.class})
	private String expressAddress;

	/** 快递公司 */
	@NotEmpty(groups = {Insert.class, Update.class})
	private String expressCompany;

	/** 快递编号 */
	@NotEmpty(groups = {Insert.class, Update.class})
	private String expressNo;

	private String remark;

	/** 打印次数 */
	private Integer printNumber;

	private String productDetail;

	private String userName;

	private List<OrderExpressDetail> orderExpressDetails;

}
