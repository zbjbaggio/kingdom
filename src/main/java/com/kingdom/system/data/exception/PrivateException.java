package com.kingdom.system.data.exception;

import com.kingdom.system.data.enmus.ErrorInfo;
import lombok.ToString;

/**
 * controller 异常
 * Created by jay on 2016-10-25.
 */
@ToString
public class PrivateException extends RuntimeException {

    public int code;

    public String msg;

    public PrivateException(ErrorInfo errorInfo) {
        this.code = errorInfo.getValue();
        this.msg = errorInfo.getName();
    }
}