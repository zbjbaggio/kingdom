package com.kingdom.system.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 描述：
 * Created by jay on 2017-12-13.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PermissionDTO {


    private Long id;    @NotNull

    private String name;

    private boolean available;

    private Long parentId;

    private String code;

    List<PermissionDTO> children;
}
