package com.kingdom.system.controller.advice.web;

import com.kingdom.system.data.entity.UserEntity;
import com.kingdom.system.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/web/")
@Slf4j
public class WebLoginController {

    @Inject
    private UserServiceImpl userService;

    @PostMapping("/login")
    public UserEntity login(@RequestBody @Validated({UserEntity.Login.class}) UserEntity userEntity, BindingResult bindingResult) throws Exception {
        return userService.login(userEntity);
    }

}
