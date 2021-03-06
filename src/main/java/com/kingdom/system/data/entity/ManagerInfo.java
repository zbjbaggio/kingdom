package com.kingdom.system.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kingdom.system.constant.RegularExpressionConstant;
import com.kingdom.system.data.base.EntityBase;
import com.kingdom.system.data.vo.RoleVO;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 用户表
 * Created by jay on 2017-4-7.
 */
@Data
@ToString(callSuper = true)
public class ManagerInfo extends EntityBase implements Serializable {

    private Long id;

    @NotEmpty(groups = {BaseInfo.class, LoginGroup.class}, message = "用户名不能为空！")
    @Length(max = 30, message = "用户名不能超过30个字！")
    private String username;

    @NotEmpty(groups = {BaseInfo.class})
    @Pattern(regexp = RegularExpressionConstant.EMAIL, groups = {BaseInfo.class}, message = "邮箱格式不正确！")
    private String email;

    @NotEmpty(groups = {BaseInfo.class})
    private String name;

    @NotEmpty(groups = {LoginGroup.class}, message = "用户密码不能为空！")
    @Length(max = 30, message = "密码不能超过50个字！")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotEmpty(groups = {BaseInfo.class})
    @Length(max = 11, min = 11, groups = {BaseInfo.class}, message = "手机格式不正确！")
    private String phone;

    private String salt;

    @NotNull(groups = {BaseInfo.class}, message = "用户状态不能为空！")
    private byte status;

    private Long operatorId;

    private String key;

    private String token;

    private int passwordNumber;//密码猜测次数

    private Set<String> permissionSet;

    private List<RoleVO> roleInfos;

    private long superAdmin;

    public interface LoginGroup {
    }

    public interface BaseInfo {
    }

}
