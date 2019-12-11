package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.ann.RequiresPermissions;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.entity.Category;
import com.kingdom.system.data.entity.ProductRemark;
import com.kingdom.system.service.impl.CategoryServiceImpl;
import com.kingdom.system.service.impl.ExchangeRateRecordServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/user/category")
@RequiresPermissions("/manage/user/category")
@Slf4j
public class CategoryController extends BaseController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
        startPage();
        return getDataTable(categoryService.list());
    }

    @PostMapping("/update")
    public void update(@RequestBody @Validated({Category.Update.class, Category.BaseInfo.class}) Category category, BindingResult bindingResult) {
        categoryService.update(category);
    }

}
