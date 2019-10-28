package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.dto.OrderDTO;
import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.ManagerInfo;
import com.kingdom.system.data.entity.OrderInfo;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.ManagerVO;
import com.kingdom.system.data.vo.OrderVO;
import com.kingdom.system.service.impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 订单信息
 */
@RestController
@RequestMapping("/manage/user/order")
@Slf4j
public class OrderController extends BaseController {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @PostMapping(value = "/insert")
    public OrderDTO insert(@RequestBody @Validated(OrderInfo.Insert.class) OrderDTO orderDTO, BindingResult bindingResult) throws Exception {
        return orderServiceImpl.insert(orderDTO);
    }

    @GetMapping(value = "/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "") String sendDateStart, @RequestParam(defaultValue = "") String sendDateEnd) {
        startPage();
        return getDataTable(orderServiceImpl.list(search == "" ? "" : "%" + search + "%", sendDateStart, sendDateEnd));
    }

    @GetMapping(value = "/detail/{orderId}")
    public OrderVO detail(@PathVariable(value = "orderId") Long orderId) {
        return orderServiceImpl.detail(orderId);
    }

}
