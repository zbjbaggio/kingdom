package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.ann.RequiresPermissions;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.entity.NoProductDetail;
import com.kingdom.system.data.entity.NoProductParent;
import com.kingdom.system.data.entity.Product;
import com.kingdom.system.service.impl.NoProductServiceImpl;
import com.kingdom.system.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manage/user/noProduct")
@RequiresPermissions("/manage/user/noProduct")
public class NoProductController extends BaseController {

    @Autowired
    private NoProductServiceImpl noProductService;

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(defaultValue = "") String date) {
        startPage();
        return getDataTable(noProductService.listNoProduct(date));
    }

    @PostMapping("/insert")
    public void insert(@RequestBody @Validated({NoProductParent.Insert.class, NoProductParent.BaseInfo.class}) NoProductParent noProductParent, BindingResult bindingResult) {
        noProductService.insertNoProduct(noProductParent);
    }

    @PostMapping("/insertDetail")
    public void insertDetail(@RequestBody @Validated({NoProductDetail.Insert.class, NoProductDetail.BaseInfo.class}) NoProductDetail noProductDetail, BindingResult bindingResult) {
        noProductService.insertNoProductDetail(noProductDetail);
    }

    @GetMapping("/listDetail/{noProductId}")
    public List<NoProductDetail> listDetail(@PathVariable(value = "noProductId") Long noProductId) {
        return noProductService.listDetail(noProductId);
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
