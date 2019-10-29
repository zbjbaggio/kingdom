package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

    
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
	private Long orderId;
	/** 收货人姓名 */
	private String expressName;
	/** 收货地址 */
	private String expressAddress;
	/** 快递公司 */
	private String expressCompany;
	/** 快递编号 */
	private String expressNo;
	/** 打印次数 */
	private Integer printNumber;

}
