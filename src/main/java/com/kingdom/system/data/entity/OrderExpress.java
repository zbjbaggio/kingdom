package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import com.kingdom.system.util.excel.ExcelExport;
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

	@ExcelExport(name = "序号")
	private String no;

	@ExcelExport(name = "用户/平台订单号")
	private String name;

	@ExcelExport(name = "收件公司名称")
	private String compay;

	/** 收货人姓名 */
	@ExcelExport(name = "收件人姓名")
	@NotEmpty(groups = {Insert.class, Update.class})
	private String expressName;

	@ExcelExport(name = "收件人电话")
	private String telephone;


	@ExcelExport(name = "收件人手机")
	private Double phone;

	/** 收货人电话 */
	@NotEmpty(groups = {Insert.class, Update.class})
	private String expressPhone;

	/** 收货地址 */
	@ExcelExport(name = "收货详细地址 *")
	@NotEmpty(groups = {Insert.class, Update.class})
	private String expressAddress;

	@ExcelExport(name = "运输性质")
	private String moveType;

	@ExcelExport(name = "运费付款方式")
	private String payType;

	@ExcelExport(name = "送货方式")
	private String type;

	@ExcelExport(name = "货物名称")
	private String productName;

	@ExcelExport(name = "货物件数")
	private String number;

	@ExcelExport(name = "货物包装")
	private String page;

	@ExcelExport(name = "货物重量")
	private String count;

	@ExcelExport(name = "货物体积")
	private String hwtj;

	@ExcelExport(name = "其他费用")
	private String qtfy;

	@ExcelExport(name = "保价金额")
	private String bjje;

	@ExcelExport(name = "代收退款方式")
	private String dstkfs;

	@ExcelExport(name = "代收金额")
	private String dsje;

	@ExcelExport(name = "开户姓名")
	private String khxm;

	@ExcelExport(name = "代收账号")
	private String dszh;

	@ExcelExport(name = "签收单")
	private String qsd;

	@ExcelExport(name = "备注信息")
	private String bjxx;

	/** 订单主表id */
	@NotNull(groups = {Insert.class, Update.class})
	private Long orderId;



	private Long userSendAddressId;

	/** 订货人id */
	@NotNull(groups = {Insert.class, Update.class})
	private Long orderUserId;





	/** 快递公司 */
	@NotEmpty(groups = {Insert.class, Update.class})
	private String expressCompany;

	/** 快递编号 */
	//@NotEmpty(groups = {Insert.class, Update.class})
	private String expressNo;

	private String remark;

	/** 打印次数 */
	private Integer printNumber;

	private String productDetail;

	private String userName;

	private List<OrderExpressDetail> orderExpressDetails;

}
