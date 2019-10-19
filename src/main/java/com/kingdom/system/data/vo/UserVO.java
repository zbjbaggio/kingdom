package com.kingdom.system.data.vo;

import com.kingdom.system.data.entity.UserEntity;
import com.kingdom.system.data.entity.UserSendAddress;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class UserVO extends UserEntity {

    private List<UserSendAddress> userSendAddressList;

}
