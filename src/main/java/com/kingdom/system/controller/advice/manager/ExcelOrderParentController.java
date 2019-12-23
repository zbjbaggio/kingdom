package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.data.dto.OrderExcelDTO;
import com.kingdom.system.data.dto.OrderExcelPayDTO;
import com.kingdom.system.service.impl.OrderServiceImpl;
import com.kingdom.system.util.excel.ExcelUtil;
import com.kingdom.system.util.excel.HssfExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/manage/orderParent/")
@Slf4j
public class ExcelOrderParentController {

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping(value = "excelExportOrder")
    public void excelExport(HttpServletResponse response, @RequestParam(value = "orderParentId") Long orderParentId) throws Exception {
        List<OrderExcelDTO> orderExcelDTOS = orderService.listOrderExcel(orderParentId);
        if (orderExcelDTOS != null && orderExcelDTOS.size() > 0) {
            ExcelUtil excelUtil = new HssfExcelUtil();
            excelUtil.writeExcel(response, "订货订单", "订货订单", orderExcelDTOS);
        }
    }

    @GetMapping(value = "excelExportPay")
    public void excelExportPay(HttpServletResponse response, @RequestParam(value = "orderParentId") Long orderParentId) throws Exception {
        List<OrderExcelPayDTO> orderExcelDTOS = orderService.listOrderPayExcel(orderParentId);
        if (orderExcelDTOS != null && orderExcelDTOS.size() > 0) {
            ExcelUtil excelUtil = new HssfExcelUtil();
            excelUtil.writeExcel(response, "付款订单", "付款订单", orderExcelDTOS);
        }
    }

    @GetMapping(value = "excelExport")
    public void excelExport(HttpServletResponse response, @RequestParam(value = "startDate", defaultValue = "")String startDate,
                            @RequestParam(value = "endDate", defaultValue = "")String endDate) throws Exception {
        List all = orderService.liseExpress(startDate, endDate);
        ExcelUtil excelUtil = new HssfExcelUtil();
        excelUtil.writeExcel(response, "发货订单", "发货订单", all);

    }
}
