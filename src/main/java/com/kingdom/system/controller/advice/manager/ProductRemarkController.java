package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.ann.RequiresPermissions;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.entity.Product;
import com.kingdom.system.data.entity.ProductRemark;
import com.kingdom.system.service.impl.ProductRemarkServiceImpl;
import com.kingdom.system.service.impl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manage/user/productRemark")
@Slf4j
@RequiresPermissions("/manage/user/productRemark")
public class ProductRemarkController extends BaseController {

    @Autowired
    private ProductRemarkServiceImpl productRemarkService;

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(defaultValue = "") String search) {
        startPage();
        return getDataTable(productRemarkService.listProductRemark("".equals(search) ? "" : "%" + search + "%"));
    }

    @PostMapping("/insert")
    public ProductRemark insert(@RequestBody @Validated({ProductRemark.Insert.class, ProductRemark.BaseInfo.class}) ProductRemark productRemark, BindingResult bindingResult) {
        return productRemarkService.insertProductRemark(productRemark);
    }

    /*@PostMapping("/update")
    public ProductRemark update(@RequestBody @Validated({ProductRemark.Update.class, ProductRemark.BaseInfo.class}) ProductRemark productRemark, BindingResult bindingResult) {
        return productRemarkService.updateProductRemark(productRemark);
    }

    @PostMapping("/remove")
    public void remove(@RequestParam Long[] ids) {
        productRemarkService.remove(ids);
    }*/


    @GetMapping("/productList")
    public List<Product> productList() {
        return productService.productList();
    }

}
