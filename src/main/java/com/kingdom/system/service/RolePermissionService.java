package com.kingdom.system.service;

import com.kingdom.system.data.entity.RolePermission;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：
 * Created by jay on 2018-1-15.
 */
public interface RolePermissionService {
    @Transactional(rollbackFor = Exception.class)
    List<RolePermission> savePermission(RolePermission rolePermission) throws Exception;
}
