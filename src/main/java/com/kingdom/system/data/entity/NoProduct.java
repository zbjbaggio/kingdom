package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class NoProduct extends EntityBase implements Serializable {

    @NotNull(groups = NoProductParent.BaseInfo.class)
    private Long productId;

    private String productName;

    @NotNull(groups = NoProductParent.BaseInfo.class)
    private Integer number;

    private String remark;

    private Integer olderNumber;

    private Long noProductParentId;

    public interface BaseInfo {

    }
}
