package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.constant.DrConstants;
import com.kingdom.system.controller.advice.BaseController;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.entity.UserEntity;
import com.kingdom.system.data.entity.UserSendAddress;
import com.kingdom.system.data.vo.UserVO;
import com.kingdom.system.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
     * @param pageNum 页数
     * @param pageSize 页大小
     * @param search 查询条件
     * @return 页对象
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize,
                              @RequestParam(value = "search") String search) {
        startPage();
        return getDataTable(userService.list(search));
    }

    /**
     * 新增
     * @param user 用户信息
     * @param bindingResult 校验结果
     * @return 用户信息
     */
    @PostMapping("/add")
    public UserEntity add(@RequestBody @Validated({UserEntity.Insert.class, UserEntity.BaseInfoSave.class}) UserEntity user,
                               BindingResult bindingResult) throws Exception {
        return userService.insert(user);
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @param bindingResult 校验结果
     */
    @PostMapping("/update")
    public UserEntity update(@RequestBody @Validated({UserEntity.Update.class, UserEntity.BaseInfoSave.class}) UserEntity user,
                           BindingResult bindingResult) throws Exception {
        return userService.updateUser(user);
    }

    /**
     * 用户信息详情
     * @param userId 用户id
     * @return 用户详情+收货地址
     */
    @GetMapping("/getDetail/{userId}")
    public UserVO getDetail(@PathVariable(value = "userId") Long userId) {
        return userService.getDetail(userId);
    }

    /**
     * 用户删除
     * @param id 用户id
     */
    @PostMapping("/delete/{id}")
    public void deleteUser(@PathVariable(value = "id") String id) {
        userService.updateDr(id, DrConstants.DELETE);
    }

    /**
     * 保存用户收货地址
     * @param userSendAddressEntity 收货地址实体
     * @param bindingResult 校验结果
     * @return 用户说话地址
     */
    @PostMapping("/saveSendAddress")
    public UserSendAddress saveSendAddress(@RequestBody @Validated(value = {UserSendAddress.SaveBaseInfo.class,
            UserSendAddress.Insert.class}) UserSendAddress userSendAddressEntity, BindingResult bindingResult) {
       return userService.insertSendAddress(userSendAddressEntity);
    }

    /**
     * 删除用户收货地址
     * @param sendAddressId 收货地址id
     */
    @GetMapping("/deleteSendAddress/{sendAddressId}")
    public void deleteSendAddress(@PathVariable(value = "sendAddressId") String sendAddressId) {
        userService.updateSendAddressDr(sendAddressId, 1);
    }


}
