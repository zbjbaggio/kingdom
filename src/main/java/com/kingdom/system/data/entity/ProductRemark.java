package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString(callSuper = true)
public class ProductRemark extends EntityBase implements Serializable {

    @NotNull(groups = BaseInfo.class)
    private Long productId;

    @NotNull(groups = BaseInfo.class)
    private Date date;

    @NotEmpty(groups = BaseInfo.class)
    private String productName;

    @NotNull(groups = BaseInfo.class)
    private Long number;

    private String remark;

    public interface BaseInfo {

    }
}
