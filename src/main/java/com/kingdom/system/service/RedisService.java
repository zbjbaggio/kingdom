package com.kingdom.system.service;

import com.kingdom.system.data.entity.ManagerInfo;
import com.kingdom.system.data.entity.UserEntity;

/**
 * 描述：redis 服务
 * Created by jay on 2017-9-28.
 */
public interface RedisService {

    String USER_TOKEN_KEY = "user_token";//用户登录后存入redis的key

    String USER_PASSWORD_NUMBER_KEY = "user_password";//用户猜密码次数存入redis的key

    String USER_LOCKED_NUMBER_KEY = "user_locked_number";//用户欲锁定次数

    String MOBILE_TOKEN_KEY = "mobile_token";//手机端用户登录后存入redis的key

    void saveMobile(UserEntity userEntity);

    /**
     * FUNCTION
     */

    void saveUser(ManagerInfo managerInfo);

    //ManagerInfo getUserInfo(ManagerInfo userInfo) throws Exception;

    void saveUserPasswordNumber(String username, Integer number);


    Integer getUserExpectNumber(String key);

    Integer getUserPasswordNumber(String username);


    ManagerInfo getUserInfoByKey(String key);

    UserEntity getMobileByKey(String key);

    void removeUserTokenByKey(String key);

    void removeUserPasswordNumberByKey(String key);


    void saveUserExpectNumber(String username, int lockedNumber);
}
