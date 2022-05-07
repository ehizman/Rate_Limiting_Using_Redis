package com.ehizman.mmr_application.services;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
@Slf4j
public class RedisRateLimiterImpl implements RedisRateLimiter {
    @Autowired
    private JedisPool jedisPool;

    private static final int REQUESTS_PER_24_HOURS = 50;

    private boolean isNotAllowed(String key) {
        try(Jedis jedis = jedisPool.getResource()){
            String requests = jedis.get(key);
            log.info("Key -> {}", key);
            if (StringUtils.isNotBlank(requests) && Integer.parseInt(requests) >= REQUESTS_PER_24_HOURS) {
                return true;
            }
            jedis.incr(key);
            jedis.expire(key, Long.valueOf(24*60*60));
            log.info("Current request count: " + jedis.get(key));
            return false;
        }
    }

    public boolean isLimitExceeded(String key) {
       return isNotAllowed(key);
    }
}
