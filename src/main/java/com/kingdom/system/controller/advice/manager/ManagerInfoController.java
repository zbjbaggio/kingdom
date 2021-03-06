package com.kingdom.system.controller.advice.manager;

import com.kingdom.system.ann.RequiresPermissions;
import com.kingdom.system.data.base.Page;
import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.enmus.UserStatus;
import com.kingdom.system.data.entity.ManagerInfo;
import com.kingdom.system.data.entity.ManagerRole;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.ManagerVO;
import com.kingdom.system.data.vo.RoleVO;
import com.kingdom.system.service.ManagerInfoService;
import com.kingdom.system.service.ManagerRoleService;
import com.kingdom.system.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * 描述：后台管理员管理
 * Created by jay on 2017-9-29.
 */
@RestController
@RequestMapping("/manage/user/managerInfo")
@Slf4j
@RequiresPermissions("/manage/user/managerInfo")
public class ManagerInfoController {

    @Inject
    private ManagerInfoService managerInfoService;

    @Inject
    private RoleService roleService;

    @Inject
    private ManagerRoleService managerRoleService;

    /**
     * 用户查询
     * @param limit
     * @param offset
     * @param searchStr
     * @param status
     * @return
     */
    @GetMapping(value = "/list")
    public Page list(@RequestParam(value = "limit", defaultValue = "10") int limit,
                     @RequestParam(value = "offset", defaultValue = "0") int offset,
                     @RequestParam(value = "searchStr", defaultValue = "-1") String searchStr,
                     @RequestParam(value = "status", defaultValue = "-1") int status,
                     @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                     @RequestParam(value = "desc") boolean desc) {
        return managerInfoService.listPage(limit, offset, searchStr, status, orderBy, desc);
    }

    /**
     * 添加管理员
     *
     * @param managerInfo
     * @return
     */
    @PostMapping(value = "/save")
    public ManagerInfo save(@RequestBody @Validated(ManagerInfo.BaseInfo.class) ManagerInfo managerInfo, BindingResult bindingResult) throws Exception {
        if (managerInfo.getId() == null && managerInfo.getPassword() == null) {
            log.info("新增是密码没给, managerInfo:{}", managerInfo);
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        return managerInfoService.save(managerInfo);
    }

    /**
     * 管理员详情
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/getDetail")
    public ManagerVO getDetail(@RequestParam(value = "userId") Long userId) {
        return managerInfoService.getDetail(userId);
    }

    /**
     * 管理员锁定
     *
     * @param userId
     * @throws Exception
     */
    @PostMapping(value = "/updateFreeze")
    public void updateFreeze(@RequestParam Long userId) throws Exception {
        if (userId == null) {
            log.info("userId为空！");
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        managerInfoService.updateStatus(userId, UserStatus.LOCKED);
    }

    /**
     * 管理员解锁
     *
     * @param userId
     * @throws Exception
     */
    @PostMapping(value = "/unlocked")
    public void unlocked(@RequestParam Long userId) throws Exception {
        if (userId == null) {
            log.info("userId为空！");
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        managerInfoService.unlockedUserStatus(userId);
    }

    /**
     * 管理员删除
     *
     * @param userIds
     * @throws Exception
     */
    @PostMapping(value = "/remove")
    public void remove(@RequestParam Long[] userIds) throws Exception {
        if (userIds == null || userIds.length <= 0) {
            log.info("userIds为空！");
            throw new PrivateException(ErrorInfo.PARAMS_ERROR);
        }
        managerInfoService.remove(userIds);
    }

    @GetMapping(value = "/listRole")
    public List<RoleVO> listRole(@RequestParam Long userId) {
        return roleService.listAllByUserId(userId);
    }

    @PostMapping(value = "/saveRoles")
    public void saveRoles(@RequestBody @Validated(value = ManagerRole.Manager.class) ManagerRole managerInfo, BindingResult bindingResult) throws Exception {
        managerRoleService.saves(managerInfo);
    }

}
