package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.ann.RequiresPermissions;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.dto.OrderDTO;
import com.kingdom.system.data.dto.OrderDetailDTO;
import com.kingdom.system.data.dto.OrderExcelDTO;
import com.kingdom.system.data.dto.OrderExpressDTO;
import com.kingdom.system.data.entity.*;
import com.kingdom.system.data.vo.OrderDetailVO;
import com.kingdom.system.data.vo.OrderVO;
import com.kingdom.system.service.impl.ExchangeRateRecordServiceImpl;
import com.kingdom.system.service.impl.OrderParentServiceImpl;
import com.kingdom.system.service.impl.OrderServiceImpl;
import com.kingdom.system.service.impl.UserServiceImpl;
import com.kingdom.system.util.excel.ExcelUtil;
import com.kingdom.system.util.excel.HssfExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单信息
 */
@RestController
@RequestMapping("/manage/user/orderParent/")
@Slf4j
@RequiresPermissions("/manage/user/orderParent")
public class OrderParentController extends BaseController {

    @Autowired
    private OrderParentServiceImpl orderParentService;

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping(value = "/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String startDate,
                              @RequestParam(defaultValue = "") String endDate) {
        startPage();
        return getDataTable(orderParentService.list("".equals(name) ? "" : "%" + name + "%", startDate, endDate));
    }

    @GetMapping(value = "/detail/{parentOrderId}")
    public List<OrderInfo> detail(@PathVariable(value = "parentOrderId") Long parentOrderId) {
        return orderService.detailByParentOrderId(parentOrderId);
    }

    @RequestMapping(value = "excelExportOrder", method = RequestMethod.GET)
    public void excelExport(HttpServletResponse response, @RequestParam(value = "orderParentId") Long orderParentId) throws Exception {
        List<OrderExcelDTO> orderExcelDTOS = orderService.listOrderExcel(orderParentId);
        if (orderExcelDTOS != null && orderExcelDTOS.size() > 0) {
            ExcelUtil excelUtil = new HssfExcelUtil();
            excelUtil.writeExcel(response, "订单", "订单", orderExcelDTOS);
        }
    }

}
