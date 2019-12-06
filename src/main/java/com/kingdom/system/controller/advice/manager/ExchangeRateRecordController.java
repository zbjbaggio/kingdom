package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.ann.RequiresPermissions;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.entity.ExchangeRateRecord;
import com.kingdom.system.data.entity.UserEntity;
import com.kingdom.system.service.impl.ExchangeRateRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 汇率记录 信息操作处理
 *
 * @author kingdom
 * @date 2019-10-19
 */
@Controller
@RequestMapping("/manage/user/exchangeRateRecord")
@RequiresPermissions("/manage/user/exchangeRateRecord")
public class ExchangeRateRecordController extends BaseController {

    @Autowired
    private ExchangeRateRecordServiceImpl exchangeRateRecordService;

    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(value = "date") String date) {
        startPage();
        return getDataTable(exchangeRateRecordService.list(date));
    }

    @PostMapping("/add")
    @ResponseBody
    public ExchangeRateRecord add(@RequestBody @Validated({ExchangeRateRecord.Insert.class})ExchangeRateRecord exchangeRateRecord,
                                  BindingResult bindingResult) {
        return exchangeRateRecordService.add(exchangeRateRecord);
    }

}
