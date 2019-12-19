package com.kingdom.system.controller.advice.web;

import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.dto.OrderExpressDTO;
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
                List<OrderUser> users = orderService.listOrderUser(orderIds);
                List<OrderProduct> orderProducts = orderService.listOrderProductByIds(orderIds);
                Map<Long, List<OrderProduct>> orderProductMap = new HashMap<>();
                orderProducts.forEach(orderProduct -> {
                    List<OrderProduct> orderProducts1 = orderProductMap.get(orderProduct.getOrderUserId());
                    if (orderProducts1 == null) {
                        orderProducts1 = new ArrayList<>();
                    }
                    orderProducts1.add(orderProduct);
                    orderProductMap.put(orderProduct.getOrderUserId(), orderProducts1);
                });
                Map<Long, List<OrderUser>> orderUserMap = new HashMap<>();
                users.forEach(user ->{
                    user.setOrderProducts(orderProductMap.get(user.getId()));
                    List<OrderUser> orderUsers = orderUserMap.get(user.getOrderId());
                    if (orderUsers == null) {
                        orderUsers = new ArrayList<>();
                    }
                    orderUsers.add(user);
                    orderUserMap.put(user.getOrderId(), orderUsers);
                });

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
                List<OrderExpress> orderExpressList = orderService.listExpressByIds(orderIds);
                List<OrderExpressDetail> orderExpressDetailList = orderService.listExpressDetialByIds(orderIds);
                Map<Long, List<OrderExpressDetail>> orderExpressDetailMap = new HashMap<>();
                if (orderExpressDetailList != null && orderExpressDetailList.size() > 0) {
                    orderExpressDetailList.forEach(orderExpressDetail -> {
                        List<OrderExpressDetail> orderExpressDetailList1 = orderExpressDetailMap.get(orderExpressDetail.getOrderExpressId());
                        if (orderExpressDetailList1 == null) {
                            orderExpressDetailList1 = new ArrayList<>();
                        }
                        orderExpressDetailList1.add(orderExpressDetail);
                        orderExpressDetailMap.put(orderExpressDetail.getOrderExpressId(), orderExpressDetailList1);
                    });
                }
                Map<Long, List<OrderExpress>> orderExpressMap = new HashMap<>();
                orderExpressList.forEach(orderExpress -> {
                    orderExpress.setOrderExpressDetails(orderExpressDetailMap.get(orderExpress.getId()));
                    List<OrderExpress> orderExpresses = orderExpressMap.get(orderExpress.getOrderId());
                    if (orderExpresses == null) {
                        orderExpresses = new ArrayList<>();
                    }
                    orderExpresses.add(orderExpress);
                    orderExpressMap.put(orderExpress.getOrderId(), orderExpresses);
                });
                List<OrderDetailVO> orderDetailVOs = orderService.listProductByIds(orderIds);
                Map<Long, List<OrderDetailVO>> detailMap = new HashMap<>();
                for (OrderDetailVO orderProduct : orderDetailVOs) {
                    List<OrderDetailVO> orderDetailVOS = detailMap.get(orderProduct.getOrderId());
                    if (orderDetailVOS == null) {
                        orderDetailVOS = new ArrayList<>();
                    }
                    orderDetailVOS.add(orderProduct);
                    detailMap.put(orderProduct.getOrderId(), orderDetailVOS);
                }
                for (OrderInfo row : rows) {
                    row.setOrderUsers(orderUserMap.get(row.getId()));
                    row.setOrderDetailVOS(detailMap.get(row.getId()));
                    row.setOrderPayments(orderPaymentMap.get(row.getId()));
                    row.setOrderExpresses(orderExpressMap.get(row.getId()));
                }
                return dataTable;
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
