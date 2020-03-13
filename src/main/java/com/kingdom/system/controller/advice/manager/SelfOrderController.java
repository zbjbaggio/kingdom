package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.ann.RequiresPermissions;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.dto.OrderDTO;
import com.kingdom.system.data.entity.*;
import com.kingdom.system.data.vo.OrderDetailVO;
import com.kingdom.system.service.impl.ExchangeRateRecordServiceImpl;
import com.kingdom.system.service.impl.OrderServiceImpl;
import com.kingdom.system.service.impl.ProductServiceImpl;
import com.kingdom.system.service.impl.UserServiceImpl;
import com.kingdom.system.util.ValueHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自查订单信息
 *
 * 自己只能查到自己填写的订单
 */
@RestController
@RequestMapping("/manage/user/selfOrder")
@Slf4j
@RequiresPermissions("/manage/user/selfOrder")
public class SelfOrderController extends BaseController {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @Autowired
    private ValueHolder valueHolder;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ExchangeRateRecordServiceImpl exchangeRateRecordService;

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping(value = "/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(defaultValue = "") String payUser, @RequestParam(defaultValue = "") String orderUser,
                              @RequestParam(defaultValue = "") String express, @RequestParam(defaultValue = "") String startDate,
                              @RequestParam(defaultValue = "") String endDate) {
        startPage();
        TableDataInfo dataTable = getDataTable(orderServiceImpl.list(valueHolder.getUserIdHolder().getId(), "".equals(payUser) ? "" : "%" + payUser + "%",
                "".equals(orderUser) ? "" : "%" + orderUser + "%", "".equals(express) ? "" : "%" + express + "%", startDate, endDate));
        List<OrderInfo> rows = (List<OrderInfo>) dataTable.getRows();
        if (rows != null && rows.size() > 0) {
            List<Long> orderIds = new ArrayList<>();
            for (OrderInfo row : rows) {
                orderIds.add(row.getId());
            }
            List<OrderDetailVO> orderProducts = orderServiceImpl.listProductByIds(orderIds);
            Map<Long, List<OrderDetailVO>> orderProductMap = new HashMap<>();
            for (OrderDetailVO orderProduct : orderProducts) {
                List<OrderDetailVO> orderDetailVOS = orderProductMap.get(orderProduct.getOrderId());
                if (orderDetailVOS == null) {
                    orderDetailVOS = new ArrayList<>();
                }
                orderDetailVOS.add(orderProduct);
                orderProductMap.put(orderProduct.getOrderId(), orderDetailVOS);
            }
            List<OrderPayment> orderPayments = orderServiceImpl.listOrderPaymentByIds(orderIds);
            Map<Long, List<OrderPayment>> orderPaymentMap = new HashMap<>();
            for (OrderPayment orderPayment : orderPayments) {
                List<OrderPayment> orderPaymentList = orderPaymentMap.get(orderPayment.getOrderId());
                if (orderPaymentList == null) {
                    orderPaymentList = new ArrayList<>();
                }
                orderPaymentList.add(orderPayment);
                orderPaymentMap.put(orderPayment.getOrderId(), orderPaymentList);
            }
            for (OrderInfo row : rows) {
                row.setOrderDetailVOS(orderProductMap.get(row.getId()));
                row.setOrderPayments(orderPaymentMap.get(row.getId()));
            }
        }
        return dataTable;
    }

    @PostMapping(value = "/insert")
    public OrderDTO insert(@RequestBody @Validated(OrderInfo.Insert.class) OrderDTO orderDTO, BindingResult bindingResult) {
        OrderInfo orderInfo = orderDTO.getOrderInfo();
        orderInfo.setManagerId(valueHolder.getUserIdHolder().getId());
        return orderServiceImpl.insert(orderDTO);
    }

    @GetMapping(value = "/getOrderParent")
    public OrderParent getOrderParent() {
        return orderServiceImpl.getOrderParent();
    }

    /**
     * 截单列表查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping(value = "/listOrderParent")
    public TableDataInfo listOrderParent(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                                         @RequestParam(value = "name", defaultValue = "") String name) {
        startPage();
        return getDataTable(orderServiceImpl.listOrderParent(!"".equals(name) ? "%" + name + "%" : ""));
    }

    @GetMapping(value = "/listUser/{memberNo}")
    public List<UserEntity> listUser(@PathVariable(value = "memberNo") String memberNo) {
        return userService.listByMemberNo(memberNo);
    }

    @PostMapping("/exchangeRate")
    public ExchangeRateRecord exchangeRate() {
        return exchangeRateRecordService.selectDefault();
    }


    @GetMapping("/listAllProduct")
    public List<Product> listAllProduct(@RequestParam(defaultValue = "") String search) {
        return productService.listAllProduct("".equals(search) ? "" : "%" + search + "%");
    }

}
