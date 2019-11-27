package com.kingdom.system.service.impl;

import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.UserEntity;
import com.kingdom.system.data.entity.UserSendAddress;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.data.vo.UserVO;
import com.kingdom.system.mapper.UserMapper;
import com.kingdom.system.mapper.UserSendAddressMapper;
import com.kingdom.system.service.RedisService;
import com.kingdom.system.util.PasswordUtil;
import com.kingdom.system.util.StringUtils;
import com.kingdom.system.util.TokenUtils;
import com.kingdom.system.util.ValueHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl {

    @Inject
    private UserMapper userMapper;

    @Inject
    private UserSendAddressMapper userSendAddressMapper;

    @Inject
    private ValueHolder valueHolder;

    @Inject
    private RedisService redisService;

    public List<UserEntity> list(String search) {
        return userMapper.list(search);
    }

    public UserEntity insert(UserEntity user) throws Exception {
        checkMemberNo(user);
        updatePassword(user);
        user.setCreateTime(new Date());
        int count = userMapper.insertUser(user);
        if (count != 1) {
            log.error("保存失败！user：{}", user);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        return user;
    }

    public UserEntity updateUser(UserEntity user) throws Exception {
        checkMemberNo(user);
        if (!StringUtils.isEmpty(user.getPassword())) {
            updatePassword(user);
        }
        int count = userMapper.updateUser(user);
        if (count != 1) {
            log.error("修改失败！user：{}", user);
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        return user;
    }

    private void updatePassword(UserEntity user) throws Exception {
        UUID uuid = UUID.randomUUID();
        String salt = uuid.toString();
        user.setSalt(salt);
        user.setPassword(PasswordUtil.getPassword(user.getPassword(), salt));
    }

    private void checkMemberNo(UserEntity user) {
        if (StringUtils.isNotEmpty(user.getMemberNo())) {
            int count = userMapper.getCountByMemberNo(user);
            if (count > 0) {
                log.error("会员卡号不唯一！ user：{}", user);
                throw new PrivateException(ErrorInfo.MEMBER_NO_ERROR);
            }
        }
    }

    public UserVO getDetail(Long userId) {
        UserVO userDTO = new UserVO();
        UserEntity userEntity = userMapper.selectUserById(userId);
        BeanUtils.copyProperties(userEntity, userDTO);
        userDTO.setUserSendAddressList(userSendAddressMapper.listByUserId(userId));
        return userDTO;
    }

    public void updateDr(String id, int dr) {
        int count = userMapper.updateDr(id, dr);
        if (count != 1) {
            log.error("用户删除失败！ id：{}, dr: {}", id, dr);
            throw new PrivateException(ErrorInfo.DELETE_ERROR);
        }
    }

    public UserSendAddress insertSendAddress(UserSendAddress userSendAddressEntity) {
        // 如果是默认地址，清除其他默认
        if (userSendAddressEntity.getCommon() == 1) {

        }
        int count = userSendAddressMapper.insertUserSendAddress(userSendAddressEntity);
        if (count != 1) {
            log.error("保存失败！userSendAddressEntity：{}", userSendAddressEntity);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        return userSendAddressEntity;
    }

    public void updateSendAddressDr(String sendAddressId, int dr) {
        userSendAddressMapper.updateUserSendAddressDr(sendAddressId, dr);
    }

    public UserEntity login(UserEntity userEntity) throws Exception {
        UserEntity userData = userMapper.getByMemberNo(userEntity.getMemberNo());
        if (userData == null || userEntity.getPassword().equals(userData.getPassword())) {
            log.info("会员号或者密码错误！userEntity{}", userEntity);
            throw new PrivateException(ErrorInfo.MOBILE_LOGIN_ERROR);
        }
        userData.setPassword(null);
        userData.setToken(TokenUtils.getToken(userData));
        userData.setKey(TokenUtils.getKey(userEntity));
        redisService.saveMobile(userData);
        return userData;
    }

    public boolean checkToken(String token, String key, HandlerMethod handler) {
        if (org.springframework.util.StringUtils.isEmpty(token) || org.springframework.util.StringUtils.isEmpty(key)) {
            log.info("未登录！");
            return false;
        }
        UserEntity userEntity = redisService.getMobileByKey(key);
        if (userEntity != null && token.equals(userEntity.getToken())) {
            valueHolder.setMobileUserHolder(userEntity.getId());
            redisService.saveMobile(userEntity);
            return true;
        }
        log.info("重新登录, token:{}, key:{}, userEntity:{}！", token, key, userEntity);
        return false;
    }

    public List<UserEntity> listByMemberNo(String memberNo) {
        return userMapper.listByMemberNo("%" + memberNo + "%");
    }
}
