package com.kingdom.system.util;

import com.kingdom.system.data.entity.ManagerInfo;
import com.kingdom.system.data.entity.UserEntity;

import java.util.UUID;

/**
 * 描述：token key和token生成方法
 * Created by jay on 2017-9-28.
 */
public class TokenUtils {

    public static String getKey(ManagerInfo managerInfo) throws Exception {
        return Md5Util.MD5Encode(managerInfo.getUsername(), managerInfo.getSalt());
    }

    public static String getKey(UserEntity userEntity) throws Exception {
        return Md5Util.MD5Encode(userEntity.getMemberNo(), userEntity.getSalt());
    }

    public static String getToken(UserEntity userEntity) throws Exception {
        UUID uuid = UUID.randomUUID();
        return Md5Util.MD5Encode(uuid.toString(), userEntity.getSalt());
    }

    public static String getToken(ManagerInfo managerInfo) throws Exception {
        UUID uuid = UUID.randomUUID();
        return Md5Util.MD5Encode(uuid.toString(), managerInfo.getSalt());
    }

}
