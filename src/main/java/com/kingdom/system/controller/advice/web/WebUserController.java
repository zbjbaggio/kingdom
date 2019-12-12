package com.kingdom.system.controller.advice.web;

import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.entity.Category;
import com.kingdom.system.data.entity.ExchangeRateRecord;
import com.kingdom.system.data.entity.Product;
import com.kingdom.system.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            return getDataTable(orderService.listByUserId());
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
