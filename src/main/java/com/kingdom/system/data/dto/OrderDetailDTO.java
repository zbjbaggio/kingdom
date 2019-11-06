package com.kingdom.system.data.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class OrderDetailDTO {

    @NotNull
    private Long id;

    @NotEmpty
    private String remark;

}
