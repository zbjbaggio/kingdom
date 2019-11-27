package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.OrderProduct;
import com.kingdom.system.data.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 数据层
 *
 * @author kingdom
 * @date 2019-10-17
 */
public interface UserMapper {
    /**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    public UserEntity selectUserById(Long id);

    /**
     * 查询用户列表
     *
     * @param user 用户信息
     * @return 用户集合
     */
    public List<UserEntity> selectUserList(UserEntity user);

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(UserEntity user);

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(UserEntity user);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 结果
     */
    public int deleteUserById(Long id);

    /**
     * 批量删除用户
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserByIds(String[] ids);

    List<UserEntity> list(@Param(value = "search") String search);

    int getCountByMemberNo(UserEntity user);

    int updateDr(@Param(value = "id") String id, @Param(value = "dr") int dr);

    UserEntity selectUserByMemberNo(String orderUserMemberNo);

    int updateScore(List<OrderProduct> list);

    UserEntity getByMemberNo(@Param(value = "memberNo")String memberNo);

    List<UserEntity> listByMemberNo(@Param(value = "memberNo")String memberNo);
}