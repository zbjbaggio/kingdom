package com.kingdom.system.controller.advice.manager;


import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.dto.ProductDTO;
import com.kingdom.system.data.entity.Product;
import com.kingdom.system.service.impl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品包信息
 */
@RestController
@RequestMapping("/manage/user/productPackage")
@Slf4j
public class ProductPackageController extends BaseController {

    @Autowired
    private ProductServiceImpl productService;

    /**
     * 获取产品列表
     * @param search 名字查询
     * @param pageSize 页大小
     * @param pageNum 页数
     * @return 分页对象
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(defaultValue = "") String search) {
        startPage();
        return getDataTable(productService.listProductPackage(search == "" ? "" : "%" + search + "%"));
    }

    /**
     * 保存产品信息
     * @param product 产品对象
     * @param bindingResult 检查结果
     */
    @PostMapping("/insert")
    public ProductDTO insert(@RequestBody @Validated({Product.Insert.class}) ProductDTO product, BindingResult bindingResult) {
        return productService.insertProductPackage(product);
    }

    /**
     * 保存产品信息
     * @param product 产品对象
     * @param bindingResult 检查结果
     */
    @PostMapping("/update")
    public Product update(@RequestBody @Validated({Product.Update.class}) Product product, BindingResult bindingResult) {
        return productService.updateProductPackage(product);
    }

}
