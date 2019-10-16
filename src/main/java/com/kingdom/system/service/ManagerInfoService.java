package com.kingdom.system.service;

import com.kingdom.system.data.base.Page;
import com.kingdom.system.data.dto.PasswordDTO;
import com.kingdom.system.data.enmus.UserStatus;
import com.kingdom.system.data.entity.ManagerInfo;
import com.kingdom.system.data.vo.ManagerVO;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

/**
 * Created by jay on 2017-4-10.
 */
public interface ManagerInfoService {

    ManagerVO login(ManagerInfo user) throws Exception;

    ManagerInfo save(ManagerInfo managerInfo) throws Exception;

    boolean checkToken(String token, String key, HandlerMethod handler);

    Page listPage(int limit, int offset, String searchStr, int status, String orderBy, boolean desc);

    ManagerVO getDetail(Long userId);

    void update(ManagerInfo managerInfo) throws Exception;

    void updateStatus(Long userId, UserStatus index) throws Exception;

    void unlockedUserStatus(Long userId) throws Exception;

    void remove(Long[] userIds) throws Exception;

    void loginOut() throws Exception;

    void updatePassword(PasswordDTO passwordDTO) throws Exception;

    List<ManagerVO> listAllByRoleId(Long roleId);
}
