package com.kingdom.system.mapper;


import com.kingdom.system.data.entity.UserSendAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户发货地址 数据层
 *
 * @author kingdom
 * @date 2019-10-17
 */
public interface UserSendAddressMapper {
    /**
     * 查询用户发货地址信息
     *
     * @param id 用户发货地址ID
     * @return 用户发货地址信息
     */
    public UserSendAddress selectUserSendAddressById(Long id);

    /**
     * 查询用户发货地址列表
     *
     * @param userSendAddress 用户发货地址信息
     * @return 用户发货地址集合
     */
    public List<UserSendAddress> selectUserSendAddressList(UserSendAddress userSendAddress);

    /**
     * 新增用户发货地址
     *
     * @param userSendAddress 用户发货地址信息
     * @return 结果
     */
    public int insertUserSendAddress(UserSendAddress userSendAddress);

    /**
     * 修改用户发货地址
     *
     * @param userSendAddress 用户发货地址信息
     * @return 结果
     */
    public int updateUserSendAddress(UserSendAddress userSendAddress);

    /**
     * 删除用户发货地址
     *
     * @param id 用户发货地址ID
     * @return 结果
     */
    public int deleteUserSendAddressById(Long id);

    /**
     * 批量删除用户发货地址
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserSendAddressByIds(String[] ids);

    List<UserSendAddress> listByUserId(@Param(value = "userId") Long userId);

    int updateUserSendAddressDr(@Param(value = "sendAddressId")String sendAddressId, @Param(value = "dr")int dr);
}