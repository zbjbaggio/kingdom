package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.ann.RequiresPermissions;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.service.impl.ExchangeRateRecordServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage/user/category")
@RequiresPermissions("/manage/user/category")
@Slf4j
public class CategoryController extends BaseController {

    @Autowired
    private ExchangeRateRecordServiceImpl exchangeRateRecordService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(value = "date") String date) {
        startPage();
        return getDataTable(exchangeRateRecordService.list(date));
    }

}
