package com.kingdom.system.service.impl;

import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.UserEntity;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.mapper.UserMapper;
import com.kingdom.system.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl {

    @Inject
    private UserMapper userMapper;

    public List<UserEntity> list(String search) {
        return userMapper.list(search);
    }

    public UserEntity insert(UserEntity user) {
        checkMemberNo(user);
        user.setCreateTime(new Date());
        int count = userMapper.insertUser(user);
        if (count != 1) {
            log.error("保存失败！user：{}", user);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        return user;
    }

    public UserEntity updateUser(UserEntity user) {
        checkMemberNo(user);
        int count = userMapper.updateUser(user);
        if (count != 1) {
            log.error("修改失败！user：{}", user);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        return user;
    }

    private void checkMemberNo(UserEntity user) {
        if (StringUtils.isNotEmpty(user.getMemberNo())) {
            int count = userMapper.listByMemberNo(user);
            if (count > 0) {
                log.error("会员卡号不唯一！ user：{}", user);
                throw new PrivateException(ErrorInfo.MEMBER_NO_ERROR);
            }
        }
    }

    public UserEntity getDetail(Long userId) {
        return userMapper.selectUserById(userId);
    }

    public void updateDr(String id, int dr) {
        int count = userMapper.updateDr(id, dr);
        if (count > 0) {
            log.error("用户删除失败！ id：{}, dr: {}", id, dr);
            throw new PrivateException(ErrorInfo.DELETE_ERROR);
        }
    }
}
