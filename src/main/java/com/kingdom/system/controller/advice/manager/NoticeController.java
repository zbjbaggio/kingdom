package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.ann.RequiresPermissions;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.entity.Notice;
import com.kingdom.system.data.entity.ProductRemark;
import com.kingdom.system.service.impl.ExchangeRateRecordServiceImpl;
import com.kingdom.system.service.impl.NoticeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/user/notice")
@RequiresPermissions("/manage/user/notice")
@Slf4j
public class NoticeController extends BaseController {

    @Autowired
    private NoticeServiceImpl noticeService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(value = "search") String search) {
        startPage();
        return getDataTable(noticeService.list(search));
    }

    @GetMapping("/detail/{id}")
    public Notice getDetail(@PathVariable(value = "id") Long id) {
        return noticeService.getDetail(id);
    }

    @PostMapping("/update")
    public Notice update(@RequestBody @Validated({Notice.Update.class, Notice.BaseInfo.class}) Notice notice, BindingResult bindingResult) {
        return noticeService.update(notice);
    }

    @PostMapping("/insert")
    public Notice insert(@RequestBody @Validated({Notice.Insert.class, Notice.BaseInfo.class}) Notice notice, BindingResult bindingResult) {
        return noticeService.insert(notice);
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
         noticeService.delete(id);
    }

}
