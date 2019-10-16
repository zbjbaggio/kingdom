package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.service.IGenService;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.ManagerInfo;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.ManagerVO;
import com.kingdom.system.service.ManagerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录
 * Created by jay on 2017-4-10.
 */
@RestController
@RequestMapping("/manage")
@Slf4j
public class LoginController extends BaseController {

    @Inject
    private ManagerInfoService managerInfoService;

    /**
     * 登录接口
     *
     * @param managerInfo
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/login")
    public ManagerVO login(@RequestBody @Validated(ManagerInfo.LoginGroup.class) ManagerInfo managerInfo, BindingResult bindingResult) throws Exception {
        ManagerVO managerVO = managerInfoService.login(managerInfo);
        if (managerVO == null) {
            throw new PrivateException(ErrorInfo.LOGIN_ERROR);
        }
        return managerVO;
    }

    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(@Param(value = "pageNum") int pageNum, @Param(value = "pageSize") int pageSize) {
        startPage();
        return getDataTable(managerInfoService.listAllByRoleId(1L));
    }

    @Autowired
    private IGenService genService;

    @GetMapping("/genCode/{tableName}")
    public void genCode(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genService.generatorCode(tableName);
        genCode(response, data);
    }

    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

}
