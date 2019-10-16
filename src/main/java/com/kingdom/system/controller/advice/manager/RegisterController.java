package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.ManagerInfo;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.service.ManagerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * 注册
 * Created by jay on 2017-4-10.
 */
//@RestController
@RequestMapping
@Slf4j
public class RegisterController {

    @Inject
    private ManagerInfoService managerInfoService;

    /**
     * 注册
     * @param managerInfo
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/register")
    public void register(@RequestBody @Validated(ManagerInfo.BaseInfo.class) ManagerInfo managerInfo, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            log.info("添加验证信息{}", bindingResult);
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        managerInfo = managerInfoService.save(managerInfo);
        if (managerInfo == null) {
            throw new PrivateException(ErrorInfo.REGISTER_ERROR);
        }
    }

}
