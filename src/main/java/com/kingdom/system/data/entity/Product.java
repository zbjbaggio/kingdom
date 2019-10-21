package com.kingdom.system.data.entity;

import com.kingdom.system.ann.AllowableValue;
import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 产品表 t_product
 *
 * @author kingdom
 * @date 2019-10-19
 */
@Data
@ToString(callSuper = true)
public class Product extends EntityBase implements Serializable {

    /**
     * 产品编号
     */
    @NotEmpty(groups = {Insert.class, Update.class})
    @Length(max = 50, groups = {Insert.class, Update.class})
    private String code;

    /**
     * 产品名称
     */
    @NotEmpty(groups = {Insert.class, Update.class})
    @Length(max = 50, groups = {Insert.class, Update.class})
    private String name;

    /**
     * 状态 0 下架  1 上架
     */
    @NotNull
    @AllowableValue(intValues = {0, 1}, groups = {Insert.class, Update.class}, message = "产品状态不正确！")
    private Integer status;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 总销量
     */
    private Integer totalSale;

    /**
     * 权重优先级，越大越优先
     */
    private Integer priority;

    /**
     * 入库总量
     */
    private Integer totalIn;

    /**
     * 成本价格 港币
     */
    @NotNull(groups = {Insert.class, Update.class})
    @Min(value = 0, groups = {Insert.class, Update.class})
    private BigDecimal costPrice;

    /**
     * 销售价格 港币
     */
    @NotNull(groups = {Insert.class, Update.class})
    @Min(value = 0, groups = {Insert.class, Update.class})
    private BigDecimal sellingPrice;

    /**
     * 积分
     */
    @NotNull(groups = {Insert.class, Update.class})
    @Min(value = 0, groups = {Insert.class, Update.class})
    private Integer score;

    /**
     * 类型  0 单个产品  1 产品包
     */
    private int type;

    private String remark;

    private interface BaseInfoSave {

    }

}
