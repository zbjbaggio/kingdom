package com.kingdom.system.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 订单快递明细表 t_order_express_detail
 * 
 * @author kingdom
 * @date 2019-10-29
 */
@Data
@ToString(callSuper = true)
public class OrderExpressDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Null(groups = {OrderExpress.Insert.class})
	private Long id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-6")
	private Date createTime = new Date();

	private Date lastModified;
	/**  */
	private Long orderId;
	/**  */
	private Long orderExpressId;

	/**  */
	private Long orderDetailId;

	private Long orderUserId;

	/**  */
	@NotNull(groups = {EntityBase.Insert.class, EntityBase.Update.class})
	private Long productId;
	/**  */
	private String productName;

	/**  */
	@NotNull(groups = {EntityBase.Insert.class, EntityBase.Update.class})
	@Min(1)
	private Integer number;

	private String userName;

	private String userNo;

	private Long userId;

}
