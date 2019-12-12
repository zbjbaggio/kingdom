package com.kingdom.system.util;

import com.kingdom.system.data.entity.ManagerInfo;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;

/**
 *
 * Created by jay on 16-3-19.
 */
@Component
@Singleton
@Log4j
public class ValueHolder {

    private ThreadLocal<ManagerInfo> userIdHolder = new ThreadLocal<>();

    private ThreadLocal<Long> mobileUserHolder = new ThreadLocal<>();

    public ManagerInfo getUserIdHolder() {
        return userIdHolder.get();
    }

    public Long getMobileUserHolder() {
        return mobileUserHolder.get();
    }

    public void setUserIdHolder(ManagerInfo userIdHolder) {
        this.userIdHolder.set(userIdHolder);
    }

    public void setMobileUserHolder(Long mobileUserHolder) {
        this.mobileUserHolder.set(mobileUserHolder);
    }
}