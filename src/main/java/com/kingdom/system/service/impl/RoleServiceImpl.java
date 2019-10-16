package com.kingdom.system.service.impl;

import com.kingdom.system.service.RoleService;
import com.kingdom.system.data.base.Page;
import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.RoleInfo;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.RoleVO;
import com.kingdom.system.mapper.RoleMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.List;

/**
 * 描述：角色服务
 * Created by jay on 2017-10-12.
 */
@Service
@Log4j
public class RoleServiceImpl implements RoleService {

    @Inject
    private RoleMapper roleMapper;

    @Override
    public Page listPage(int limit, int offset, String searchStr, Boolean status, String orderBy, Boolean desc) {
        if (!"-1".equals(searchStr)) {
            searchStr = "%" + searchStr + "%";
        }
        String descStr = "";
        if (!StringUtils.isEmpty(orderBy) && desc) {
            descStr = "desc";
        }
        Page page = new Page();
        Long count = roleMapper.count(searchStr, status);
        if (count != 0) {
            page.setCount(count);
            page.setList(roleMapper.listPage(limit, offset, searchStr, status, orderBy, descStr));
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleInfo save(RoleInfo role) throws Exception {
        checkRoleName(role);
        int count;
        if (role.getId() != null) {
            count = roleMapper.update(role);
        } else {
            count = roleMapper.save(role);
        }
        if (count != 1) {
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        return role;
    }

    @Override
    public RoleVO getDetail(Long roleId) {
        return roleMapper.getDetailById(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long[] roleIds) throws Exception {
        int i = roleMapper.remove(roleIds);
        if (i != roleIds.length) {
            throw new PrivateException(ErrorInfo.DELETE_ERROR);
        }
    }

    @Override
    public List<RoleVO> listAllByUserId(Long userIdHolder) {
        return roleMapper.listAllByUserId(userIdHolder);
    }

    private void checkRoleName(RoleInfo role) throws Exception {
        if (roleMapper.countRoleName(role) > 0) {
            throw new PrivateException(ErrorInfo.NAME_SAME);
        }
    }

}
