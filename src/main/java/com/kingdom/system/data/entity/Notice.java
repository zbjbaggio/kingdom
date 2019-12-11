package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Notice extends EntityBase {

    @NotNull(groups = Notice.BaseInfo.class)
    private String title;

    @NotNull(groups = Notice.BaseInfo.class)
    private String text;

    public interface BaseInfo {

    }
}
