package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import com.kingdom.system.data.dto.OrderDTO;
import com.kingdom.system.data.vo.OrderDetailVO;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 订单下单人表 t_order_user
 * 
 * @author kingdom
 * @date 2019-10-31
 */
@Data
@ToString(callSuper = true)
public class OrderUser extends EntityBase implements Serializable {

	/** 主订单id */
	private Long orderId;
	/** 下单人id */
	private Long userId;
	/** 下单人姓名 */
	@NotEmpty(groups = {OrderInfo.Insert.class, OrderDTO.BASE.class})
	private String userName;
	/** 下单人会员号 */
	private String userNo;

	@Valid
	@NotNull(groups = {OrderInfo.Insert.class, OrderDTO.BASE.class})
	@Size(groups = {OrderInfo.Insert.class, OrderDTO.BASE.class}, min = 1)
	private List<OrderProduct> orderProducts;

	private List<OrderDetailVO> orderDetailVOs;

}
