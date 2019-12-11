package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.ann.RequiresPermissions;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.service.impl.ExchangeRateRecordServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/user/notice")
@RequiresPermissions("/manage/user/notice")
@Slf4j
public class NoticeController extends BaseController {

    @Autowired
    private ExchangeRateRecordServiceImpl exchangeRateRecordService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(value = "date") String date) {
        startPage();
        return getDataTable(exchangeRateRecordService.list(date));
    }

}
