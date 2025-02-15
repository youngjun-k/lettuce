package com.example.lettuce.global.framework.security.enums;

import java.time.Duration;

public enum TokenExpireTime {
    ACCESS_TOKEN(Duration.ofHours(6).toMillis()),
    EMAIL_VERIFICATION_TOKEN(Duration.ofHours(1).toMillis()),
    RESET_PASSWORD_TOKEN(Duration.ofMinutes(30).toMillis());

    private final long expireTime;

    TokenExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getExpireTime() {
        return expireTime;
    }
}
