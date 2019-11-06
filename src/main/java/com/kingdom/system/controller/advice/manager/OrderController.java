package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.dto.OrderDTO;
import com.kingdom.system.data.dto.OrderDetailDTO;
import com.kingdom.system.data.dto.OrderExpressDTO;
import com.kingdom.system.data.entity.OrderExpress;
import com.kingdom.system.data.entity.OrderInfo;
import com.kingdom.system.data.vo.OrderVO;
import com.kingdom.system.service.OrderService;
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

    @GetMapping(value = "/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "") String sendDateStart, @RequestParam(defaultValue = "") String sendDateEnd) {
        startPage();
        return getDataTable(orderServiceImpl.list("".equals(search) ? "" : "%" + search + "%", sendDateStart, sendDateEnd));
    }

    @GetMapping(value = "/detail/{orderId}")
    public OrderVO detail(@PathVariable(value = "orderId") Long orderId) {
        return orderServiceImpl.detail(orderId);
    }

    @PostMapping(value = "/insert")
    public OrderDTO insert(@RequestBody @Validated(OrderInfo.Insert.class) OrderDTO orderDTO, BindingResult bindingResult) {
        return orderServiceImpl.insert(orderDTO);
    }

    @PostMapping(value = "/update")
    public OrderDTO update(@RequestBody @Validated(OrderInfo.Update.class) OrderDTO orderDTO, BindingResult bindingResult) {
        return orderServiceImpl.update(orderDTO);
    }

    @PostMapping(value = "/insertExpress")
    public OrderExpressDTO insertExpress(@RequestBody @Validated(OrderExpress.Insert.class) OrderExpressDTO orderExpressDTO, BindingResult bindingResult) {
        return orderServiceImpl.insertExpress(orderExpressDTO);
    }

    @PostMapping(value = "/updateExpress")
    public OrderExpressDTO updateExpress(@RequestBody @Validated(OrderExpress.Update.class) OrderExpressDTO orderExpressDTO, BindingResult bindingResult) {
        return orderServiceImpl.updateExpress(orderExpressDTO);
    }

    @PostMapping(value = "/printNumber/{orderExpressId}")
    public void printNumber(@PathVariable(value = "orderExpressId") Long orderExpressId) {
        orderServiceImpl.printNumber(orderExpressId);
    }

    @PostMapping(value = "/updateDetail")
    public void updateDetail(@RequestBody @Validated OrderDetailDTO orderDetailDTO, BindingResult bindingResult) {
        orderServiceImpl.updateDetail(orderDetailDTO);
    }

}
