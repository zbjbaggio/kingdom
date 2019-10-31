package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

    
/**
 * 订单快递明细表 t_order_express_detail
 * 
 * @author kingdom
 * @date 2019-10-29
 */
@Data
@ToString(callSuper = true)
public class OrderExpressDetail extends EntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Long orderId;
	/**  */
	private Long orderExpressId;

	/**  */
	private Long orderDetailId;

	/**  */
	private Long productId;
	/**  */
	private String productName;
	/**  */
	private Integer number;

}
