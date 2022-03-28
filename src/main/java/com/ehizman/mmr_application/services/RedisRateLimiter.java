package com.ehizman.mmr_application.services;

public interface RedisRateLimiter {
    boolean isAllowed(String key);
    boolean isLimitExceeded(String username);
}
