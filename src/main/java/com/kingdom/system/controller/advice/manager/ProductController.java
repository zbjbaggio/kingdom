package com.kingdom.system.controller.advice.manager;


import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.entity.Product;
import com.kingdom.system.service.impl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品信息
 */
@RestController
@RequestMapping("/manage/user/product")
@Slf4j
public class ProductController extends BaseController {

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
                              @RequestParam(defaultValue = "") String search, @RequestParam String sendDateStart, @RequestParam String sendDateEnd) {
        startPage();
        return getDataTable(productService.listProduct(search == "" ? "" : "%" + search + "%", sendDateStart, sendDateEnd));
    }

    @GetMapping("/detail/{productId}")
    public Product detail(@PathVariable(value = "productId") Long productId) {
        return productService.getDetail(productId);
    }

    /**
     * 获取产品列表
     * @param search 名字查询
     * @return
     */
    @GetMapping("/listAll")
    public List<Product> listAll(@RequestParam(defaultValue = "") String search) {
        return productService.listAll(search == "" ? "" : "%" + search + "%");
    }

    /**
     * 保存产品信息
     * @param product 产品对象
     * @param bindingResult 检查结果
     */
    @PostMapping("/insert")
    public Product insert(@RequestBody @Validated({Product.Insert.class}) Product product, BindingResult bindingResult) {
        return productService.insert(product);
    }

    /**
     * 保存产品信息
     * @param product 产品对象
     * @param bindingResult 检查结果
     */
    @PostMapping("/update")
    public Product update(@RequestBody @Validated({Product.Update.class}) Product product, BindingResult bindingResult) {
        return productService.update(product);
    }

    /**
     * 关闭产品
     * @param id 产品id
     *//*
    @GetMapping("/close/{id}")
    public void close(@PathVariable("id") String id) {
        productService.updateStatus(id, 1);
    }

    *//**
     * 产品置顶
     * @param id 产品id
     *//*
    @GetMapping("/setUp/{id}")
    public void setUp(@PathVariable("id") String id) {
        productService.updatePriority(id);
    }

*/
}
