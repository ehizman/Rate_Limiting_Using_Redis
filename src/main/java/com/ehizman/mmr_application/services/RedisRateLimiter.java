package com.ehizman.mmr_application.services;

public interface RedisRateLimiter {
    boolean isLimitExceeded(String username);
}
