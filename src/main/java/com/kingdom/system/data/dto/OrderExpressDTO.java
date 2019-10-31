package com.kingdom.system.data.dto;

import com.kingdom.system.data.entity.OrderExpress;
import com.kingdom.system.data.entity.OrderExpressDetail;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class OrderExpressDTO {

    @Valid
    @NotNull(groups = {OrderExpress.Insert.class, OrderExpress.Update.class})
    private OrderExpress orderExpress;

    @Valid
    @NotNull(groups = {OrderExpress.Insert.class, OrderExpress.Update.class})
    @Size(min = 1, groups = {OrderExpress.Insert.class, OrderExpress.Update.class})
    private List<OrderExpressDetail> expressDetails;

}
