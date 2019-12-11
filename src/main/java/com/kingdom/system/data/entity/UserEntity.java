package com.kingdom.system.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kingdom.system.data.base.EntityBase;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserEntity extends EntityBase implements Serializable {

    private static final long serialVersionUID = 2916753295857490564L;

    // 用户花名
    private String showName;

    // 真实姓名
    @NotEmpty(groups = BaseInfoSave.class)
    private String realName;

    // 会员卡号
    @NotEmpty(groups = {Login.class})
    private String memberNo;

    // 电话
    @NotEmpty(groups = {BaseInfoSave.class})
    private String mobile;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;

    private String key;

    private String token;

    @NotEmpty(groups = {BaseInfoSave.class, Login.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // 备注
    private String remark;

    // 发货总次数
    private Integer totalSend;

    // 消费总金额
    private BigDecimal totalAmount;

    // 消费总次数
    private Integer totalTrans;

    // dr状态
    private int dr;

    private Long score;

    private int showOrder;

    public interface BaseInfoSave {
    }

    public interface Login {

    }

/*    public void changeTotal(Integer totalSend, BigDecimal totalAmount, Integer totalTrans) {
        if (totalSend != null) {
            this.totalSend += totalSend;
        }
        if (totalAmount != null) {
            this.totalAmount = this.totalAmount.add(totalAmount);
        }
        if (totalTrans != null) {
            this.totalTrans += totalTrans;
        }
    }*/

}
