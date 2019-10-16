package com.kingdom.system.data.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kingdom.system.jsonserializer.CustomDoubleSerialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailVO {

    private Long id;

    private Long order_id;

    private Long product_id;

    private String product_name;

    @JsonSerialize(using = CustomDoubleSerialize.class)
    private BigDecimal price;

    private Long number;

    private String size;

    @JsonSerialize(using = CustomDoubleSerialize.class)
    private BigDecimal amount;

}
