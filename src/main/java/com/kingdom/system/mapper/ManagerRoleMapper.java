package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.ManagerRole;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
 * 描述：管理员与角色关系
 * Created by jay on 2018-1-26.
 */
public interface ManagerRoleMapper {

    int saves(List<ManagerRole> saves);

    @Delete("delete from t_manager_role where manager_id = #{param1}")
    int removeByManagerId(long managerId);

    @Delete("delete from t_manager_role where role_id = #{param1}")
    int removeByRoleId(long roleId);
}
