package com.ehizman.mmr_application;

import com.ehizman.mmr_application.services.RedisRateLimiter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class RedisRateLimiterTest {
    @Autowired
    private StringRedisTemplate stringTemplate;
    @Autowired
    RedisRateLimiter redisRateLimiter;

    @Test
    void test_returnsFalseWhenRateLimiterIsExceeded(){
        ValueOperations<String, String> operations = stringTemplate.opsForValue();
        operations.set("Ehis", "53");
        boolean result = redisRateLimiter.isLimitExceeded("Ehis");
        assertFalse(result);
    }

    @Test
    void test_returnsTrueWhenRateLimiterIsNotExceeded(){
        ValueOperations<String, String> operations = stringTemplate.opsForValue();
        operations.set("Ehis", "12");
        boolean result = redisRateLimiter.isLimitExceeded("Ehis");
        assertTrue(result);
    }

    @Test
    void test_RateLimiterExpiresAfterThreeSeconds() throws InterruptedException {
        ValueOperations<String, String> operations = stringTemplate.opsForValue();
        operations.set("Ehis", "51");
        operations.getAndExpire("Ehis", 3, TimeUnit.SECONDS);
        Thread.sleep(3000);
        boolean result = redisRateLimiter.isLimitExceeded("Ehis");
        assertTrue(result);
    }
}
