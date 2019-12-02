package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
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
	@NotNull(groups = SaveBaseInfo.class)
	private String address;

	/** 是否默认 1 是 0 否 */
	@NotNull(groups = SaveBaseInfo.class)
	private int common;

	/** 电话 */
	@NotNull(groups = SaveBaseInfo.class)
	private String mobile;

	/** 收获姓名 */
	@NotNull(groups = SaveBaseInfo.class)
	private String takedName;

	/** 用户ID */
	@NotNull(groups = SaveBaseInfo.class)
	private Long userId;

	/** 备注 */
	private String remark;

	public interface SaveBaseInfo {

	}
}
