package com.kingdom.system.controller.advice.web;

import com.kingdom.system.data.dto.EmailDTO;
import com.kingdom.system.util.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 * Created by jay on 2017-11-13.
 */
@RestController
@RequestMapping("/email")
@Slf4j
public class EmailController {

    @PostMapping(value = "/email")
    public void email() throws Exception {
        EmailUtils.sendEmail(new EmailDTO("你大爷","215344388@qq.com", "大侄子","测试一下","你看看行不行"));
    }
}
