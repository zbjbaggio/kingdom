package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.PayNo;
import org.apache.ibatis.annotations.Param;

public interface PayNoMapper {

    int save(PayNo payNo);

    int count(@Param("payNo") String payNo);

}
