package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Category extends EntityBase {

    private String name;

    @NotNull(groups = Category.BaseInfo.class)
    private String value;

    private String remark;

    public interface BaseInfo {

    }
}
