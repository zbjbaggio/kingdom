package com.kingdom.system.data.dto;

import com.kingdom.system.data.entity.*;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 描述：
 * Created by jay on 2017-11-14.
 */
@Data
public class OrderDTO {

    @Valid
    @NotNull(groups = {OrderInfo.Insert.class, BASE.class})
    private OrderInfo orderInfo;

    @Valid
    @NotNull(groups = {OrderInfo.Insert.class, BASE.class})
    @Size(groups = {OrderInfo.Insert.class, BASE.class}, min = 1)
    private List<OrderPayment> orderPayments;

    @Valid
    @NotNull(groups = {OrderInfo.Insert.class, BASE.class})
    @Size(groups ={ OrderInfo.Insert.class, BASE.class}, min = 1)
    private List<OrderUser> orderUsers;

    public interface BASE {
    }


}
