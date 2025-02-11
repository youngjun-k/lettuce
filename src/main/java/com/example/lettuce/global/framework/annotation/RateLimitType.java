package com.example.lettuce.global.framework.annotation;

public enum RateLimitType {
    USER(10, 1, 60),
    EMAIL(5, 1, 3600),
    IP(10, 1, 60);

    private final int limit;
    private final int refill;
    private final int duration;

    RateLimitType(int limit, int refill, int duration) {
        this.limit = limit;
        this.refill = refill;
        this.duration = duration;
    }

    public int getLimit() {
        return limit;
    }

    public int getRefill() {
        return refill;
    }

    public int getDuration() {
        return duration;
    }
}
