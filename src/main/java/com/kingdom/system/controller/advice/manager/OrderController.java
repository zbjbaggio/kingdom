package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.ann.RequiresPermissions;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.dto.OrderDTO;
import com.kingdom.system.data.dto.OrderDetailDTO;
import com.kingdom.system.data.dto.OrderExcelImportDTO;
import com.kingdom.system.data.dto.OrderExpressDTO;
import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.*;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.OrderDetailVO;
import com.kingdom.system.data.vo.OrderVO;
import com.kingdom.system.service.impl.ExchangeRateRecordServiceImpl;
import com.kingdom.system.service.impl.OrderServiceImpl;
import com.kingdom.system.service.impl.UserServiceImpl;
import com.kingdom.system.util.ValueHolder;
import com.kingdom.system.util.excel.ExcelUtil;
import com.kingdom.system.util.excel.HssfExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单信息
 */
@RestController
@RequestMapping("/manage/user/order")
@Slf4j
@RequiresPermissions("/manage/user/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ExchangeRateRecordServiceImpl exchangeRateRecordService;

    @Autowired
    private ValueHolder valueHolder;

    @GetMapping(value = "/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(defaultValue = "") String payUser, @RequestParam(defaultValue = "") String orderUser,
                              @RequestParam(defaultValue = "") String express, @RequestParam(defaultValue = "") String startDate,
                              @RequestParam(defaultValue = "") String productName, @RequestParam(defaultValue = "1") String status,
                              @RequestParam(defaultValue = "") String endDate) {
        startPage();
        TableDataInfo dataTable = getDataTable(orderServiceImpl.list(null, "".equals(payUser) ? "" : "%" + payUser + "%",
                "".equals(orderUser) ? "" : "%" + orderUser + "%", "".equals(express) ? "" : "%" + express + "%",
                "".equals(productName) ? "" : productName, status,
                startDate, endDate));
        List<OrderInfo> rows = (List<OrderInfo>) dataTable.getRows();
        if (rows != null && rows.size() > 0) {
            List<Long> orderIds = new ArrayList<>();
            for (OrderInfo row : rows) {
                orderIds.add(row.getId());
            }
            List<OrderDetailVO> orderProducts = orderServiceImpl.listProductByIds(orderIds);
            Map<Long, List<OrderDetailVO>> orderProductMap = new HashMap<>();
            for (OrderDetailVO orderProduct : orderProducts) {
                List<OrderDetailVO> orderDetailVOS = orderProductMap.get(orderProduct.getOrderId());
                if (orderDetailVOS == null) {
                    orderDetailVOS = new ArrayList<>();
                }
                orderDetailVOS.add(orderProduct);
                orderProductMap.put(orderProduct.getOrderId(), orderDetailVOS);
            }
            List<OrderPayment> orderPayments = orderServiceImpl.listOrderPaymentByIds(orderIds);
            Map<Long, List<OrderPayment>> orderPaymentMap = new HashMap<>();
            for (OrderPayment orderPayment : orderPayments) {
                List<OrderPayment> orderPaymentList = orderPaymentMap.get(orderPayment.getOrderId());
                if (orderPaymentList == null) {
                    orderPaymentList = new ArrayList<>();
                }
                orderPaymentList.add(orderPayment);
                orderPaymentMap.put(orderPayment.getOrderId(), orderPaymentList);
            }
            for (OrderInfo row : rows) {
                row.setOrderDetailVOS(orderProductMap.get(row.getId()));
                row.setOrderPayments(orderPaymentMap.get(row.getId()));
            }
        }
        return dataTable;
    }

    @GetMapping(value = "/listUser/{memberNo}")
    public List<UserEntity> listUser(@PathVariable(value = "memberNo") String memberNo) {
        return userService.listByMemberNo(memberNo);
    }

    @GetMapping(value = "/detail/{orderId}")
    public OrderVO detail(@PathVariable(value = "orderId") Long orderId) {
        return orderServiceImpl.detail(orderId);
    }

    @PostMapping(value = "/insert")
    public OrderDTO insert(@RequestBody @Validated(OrderInfo.Insert.class) OrderDTO orderDTO, BindingResult bindingResult) {
        OrderInfo orderInfo = orderDTO.getOrderInfo();
        orderInfo.setManagerId(valueHolder.getMobileUserHolder());
        return orderServiceImpl.insert(orderDTO);
    }

    @PostMapping(value = "/update")
    public OrderDTO update(@RequestBody @Validated(OrderDTO.BASE.class) OrderDTO orderDTO, BindingResult bindingResult) {
        return orderServiceImpl.update(orderDTO);
    }

    @PostMapping(value = "/insertExpress")
    public OrderExpressDTO insertExpress(@RequestBody @Validated(OrderExpress.Insert.class) OrderExpressDTO orderExpressDTO, BindingResult bindingResult) {
        return orderServiceImpl.insertExpress(orderExpressDTO);
    }

    @PostMapping(value = "/updateExpress")
    public OrderExpressDTO updateExpress(@RequestBody @Validated(OrderExpress.Update.class) OrderExpressDTO orderExpressDTO, BindingResult bindingResult) {
        return orderServiceImpl.updateExpress(orderExpressDTO);
    }

    /**
     * 记录打印次数
     * @param orderExpressId
     */
    @PostMapping(value = "/printNumber/{orderExpressId}")
    public void printNumber(@PathVariable(value = "orderExpressId") Long orderExpressId) {
        orderServiceImpl.printNumber(orderExpressId);
    }

    /**
     * 修改备注
     * @param orderDetailDTO
     * @param bindingResult
     */
    @PostMapping(value = "/updateDetail")
    public void updateDetail(@RequestBody @Validated OrderDetailDTO orderDetailDTO, BindingResult bindingResult) {
        orderServiceImpl.updateDetail(orderDetailDTO);
    }

    /**
     * 保存截单
     * @param orderParent
     * @param bindingResult
     */
    @PostMapping(value = "/saveOrderParent")
    public void saveOrderParent(@RequestBody @Validated OrderParent orderParent, BindingResult bindingResult) {
        orderServiceImpl.orderParentSave(orderParent);
    }

    /**
     * 截单列表查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping(value = "/listOrderParent")
    public TableDataInfo listOrderParent(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                                             @RequestParam(value = "name", defaultValue = "") String name) {
        startPage();
        return getDataTable(orderServiceImpl.listOrderParent(!"".equals(name) ? "%" + name + "%" : ""));
    }

    /**
     * 截单
     * @param orderParent
     * @param bindingResult
     */
    @PostMapping(value = "/doneOrder")
    public void doneOrder(@RequestBody @Validated OrderParent orderParent, BindingResult bindingResult) {
        orderServiceImpl.doneOrder(orderParent);
    }

    /**
     * 删除
     */
    @PostMapping(value = "/delete/{orderId}")
    public void delete(@PathVariable(value = "orderId") Long orderId) {
        orderServiceImpl.delete(orderId);
    }

    /**
     * 查询订货人地址
     */
    @GetMapping(value = "/listAddress/{userId}")
    public List<UserSendAddress> listAddress(@PathVariable(value = "userId") Long userId) {
        return userService.listAddress(userId);
    }

    @GetMapping(value = "/getOrderParent")
    public OrderParent getOrderParent() {
        return orderServiceImpl.getOrderParent();
    }

    @GetMapping(value = "/getOrderParentSum")
    public List<OrderInfo> getOrderParentSum() {
        return orderServiceImpl.getOrderParentSum();
    }

    @PostMapping("/exchangeRate")
    public ExchangeRateRecord exchangeRate() {
        return exchangeRateRecordService.selectDefault();
    }

    @PostMapping(value = "importExcel")
    public void importExcel(MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        if (!is.markSupported()) {
            is = new PushbackInputStream(is, 8);
        }
        ExcelUtil excelUtil = ExcelUtil.create(is);
        List<OrderExcelImportDTO> list = excelUtil.readExcel(OrderExcelImportDTO.class);
        if (list == null || list.size() == 0) {
            throw new PrivateException(ErrorInfo.EXCEL_EMPTY);
        }
        orderServiceImpl.importExcel(list);
    }

}
