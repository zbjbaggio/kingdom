package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.constant.DrConstants;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.entity.UserEntity;
import com.kingdom.system.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/manage/user/user")
@Slf4j
public class UserController extends BaseController {

    @Inject
    private UserServiceImpl userService;

    /**
     * 列表
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(@Param(value = "pageNum") int pageNum, @Param(value = "pageSize") int pageSize,
                              @Param(value = "search") String search) {
        startPage();
        return getDataTable(userService.list(search));
    }

    /**
     * 新增
     * @param user
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    public UserEntity save(@RequestBody @Validated({UserEntity.Insert.class, UserEntity.BaseInfoSave.class}) UserEntity user,
                               BindingResult bindingResult) {
        return userService.insert(user);
    }

    /**
     * 更新用户信息
     * @param user
     * @param bindingResult
     * @throws Exception
     */
    @PostMapping("/update")
    public UserEntity update(@RequestBody @Validated({UserEntity.Update.class, UserEntity.BaseInfoSave.class}) UserEntity user,
                           BindingResult bindingResult) {
        return userService.updateUser(user);
    }

    /**
     * 用户信息详情
     * @param userId
     * @return
     */
    @GetMapping("/getDetail/{userId}")
    public UserEntity getDetail(@PathVariable(value = "userId") Long userId) {
        return userService.getDetail(userId);
    }

    /**
     * 用户删除
     * @param id
     */
    @PostMapping("/delete/{id}")
    public void deleteUser(@PathVariable(value = "id") String id) {
        userService.updateDr(id, DrConstants.DELETE);
    }



}
