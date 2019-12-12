package com.kingdom.system.controller.advice.web;

import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.entity.*;
import com.kingdom.system.data.vo.OrderDetailVO;
import com.kingdom.system.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/web/user")
@Slf4j
public class WebUserController extends BaseController {

    @Autowired
    private NoticeServiceImpl noticeService;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private ExchangeRateRecordServiceImpl exchangeRateRecordService;

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("/noticeList")
    public TableDataInfo noticeList(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
        startPage();
        return getDataTable(noticeService.list(""));
    }

    @PostMapping("/orderList")
    public TableDataInfo orderList(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
        Category category = categoryService.selectCategoryById(1L);
        if ("0".equals(category.getValue())) {
            startPage();
            TableDataInfo dataTable = getDataTable(orderService.listByUserId());
            List<OrderInfo> rows = (List<OrderInfo>) dataTable.getRows();
            if (rows != null && rows.size() > 0) {
                List<Long> orderIds = new ArrayList<>();
                for (OrderInfo row : rows) {
                    orderIds.add(row.getId());
                }
                List<OrderDetailVO> orderProducts = orderService.listProductByIds(orderIds);
                Map<Long, List<OrderDetailVO>> orderProductMap = new HashMap<>();
                for (OrderDetailVO orderProduct : orderProducts) {
                    List<OrderDetailVO> orderDetailVOS = orderProductMap.get(orderProduct.getOrderId());
                    if (orderDetailVOS == null) {
                        orderDetailVOS = new ArrayList<>();
                    }
                    orderDetailVOS.add(orderProduct);
                    orderProductMap.put(orderProduct.getOrderId(), orderDetailVOS);
                }
                List<OrderPayment> orderPayments = orderService.listOrderPaymentByIds(orderIds);
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
        }
        return null;
    }

    @PostMapping("/exchangeRate")
    public ExchangeRateRecord exchangeRate() {
        return exchangeRateRecordService.selectDefault();
    }

    @PostMapping("/productAll")
    public List<Product> productAll(@RequestParam(defaultValue = "") String search) {
        return productService.listAllProduct("".equals(search) ? "" : "%" + search + "%");
    }

}
