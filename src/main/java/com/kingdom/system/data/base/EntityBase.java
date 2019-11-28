package com.kingdom.system.data.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 * 表的基本属性
 * Created by jay on 2017-4-7.
 */
@Data
public class EntityBase implements Serializable {

    @Null(groups = {Insert.class})
    @NotNull(groups = {Update.class, Id.class})
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-6")
    private Date createTime;

    private Date lastModified;

    public interface Insert {

    }

    public interface Update {

    }

    public interface Id {

    }

}
