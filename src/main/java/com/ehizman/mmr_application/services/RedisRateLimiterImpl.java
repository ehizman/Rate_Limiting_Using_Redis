package com.ehizman.mmr_application.services;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.SessionCallback;
import java.util.List;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisRateLimiterImpl implements RedisRateLimiter {
    @Autowired
    private StringRedisTemplate stringTemplate;

    private static final int REQUESTS_PER_24_HOURS = 50;

    public boolean isAllowed(String key) {
        ValueOperations<String, String> operations = stringTemplate.opsForValue();
        String requests = operations.get(key);
        if (StringUtils.isNotBlank(requests) && Integer.parseInt(requests) >= REQUESTS_PER_24_HOURS) {
            return false;
        }
        List<Object> txResults = stringTemplate.execute(new SessionCallback<>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations){
                final StringRedisTemplate redisTemplate = (StringRedisTemplate) operations;
                final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
                operations.multi();
                valueOperations.increment(key);
                redisTemplate.expire(key, 24, TimeUnit.HOURS);
                // This will contain the results of all operations in the transaction
                return operations.exec();
            }
        });
        log.info("Current request count: " + txResults.get(0));
        return true;
    }

    public boolean isLimitExceeded(String username) {
       return isAllowed(username);
    }
}
