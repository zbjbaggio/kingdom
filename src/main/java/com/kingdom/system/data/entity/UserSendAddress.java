package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户发货地址表 t_user_send_address
 * 
 * @author kingdom
 * @date 2019-10-17
 */
@Data
@ToString(callSuper = true)
public class UserSendAddress extends EntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 地址 */
	private String address;

	/** 是否默认 0 是 1 否 */
	private Integer common;

	/** dr状态,1删除，0留用 */
	private Integer dr;

	/** 电话 */
	private String mobile;

	/** 收获姓名 */
	private String takedName;

	/** 用户ID */
	private Long userId;

	/** 备注 */
	private String remark;

}
