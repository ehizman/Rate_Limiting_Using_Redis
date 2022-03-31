package com.ehizman.mmr_application.services;

import com.ehizman.mmr_application.exceptions.APIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisUtility {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void getValue( final String key) throws APIException {
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null){
            throw new APIException(
                    String.format("sms from %s to %s blocked by STOP request",
                            key.split(":")[1], key.split(":")[0])
            );
        }
    }
    public void setValue(final String key){
        redisTemplate.opsForValue().set(key,"STOP");
        log.info("Redis template Passed this point");

        redisTemplate.expire(key, 4, TimeUnit.HOURS);
    }
}
