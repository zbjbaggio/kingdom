package com.kingdom.system.data.entity;

import com.kingdom.system.data.base.EntityBase;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    private String memberNo;

    // 电话
    private String mobile;

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

    public interface BaseInfoSave {
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
