package com.kingdom.system.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kingdom.system.constant.TimeZoneConstant;
import com.kingdom.system.data.base.EntityBase;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString(callSuper = true)
public class NoProduct extends EntityBase implements Serializable {

    @NotNull(groups = BaseInfo.class)
    private Long productId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = TimeZoneConstant.DEFAULT)
    @NotNull(groups = BaseInfo.class)
    private Date date;

    private String productName;

    @NotNull(groups = BaseInfo.class)
    private Integer number;

    private String remark;

    private Integer olderNumber;

    public interface BaseInfo {

    }
}
