package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.PayNo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayNoMapper {

    int save(PayNo payNo);

    int count(@Param("payNo") String payNo);

    int countByOrderId(@Param("list") List list, @Param("orderId") Long orderId);

    int deleteByOrderId(@Param("orderId") Long orderId);
}
