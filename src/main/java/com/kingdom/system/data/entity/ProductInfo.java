package com.kingdom.system.data.entity;


import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 产品表 tb_product
 *
 * @author kingdom
 * @date 2019-10-16
 */
@Data
@ToString(callSuper = true)
public class ProductInfo extends EntityBase implements Serializable {

    /**
     * 产品编号
     */
    private String code;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 状态
     */
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

    private String remark;

}
