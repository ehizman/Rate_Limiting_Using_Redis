package com.ehizman.mmr_application.service;

import com.ehizman.mmr_application.controller.requests_responses.Request;
import com.ehizman.mmr_application.exceptions.APIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisUtility {
    @Autowired
    RedisTemplate<String, Request> redisTemplate;

    public void getValue( final String key) throws APIException {
        Request value = redisTemplate.opsForValue().get(key);
        if (value != null){
            throw new APIException(
                    String.format("sms from %s to %s blocked by STOP request",
                            value.getFrom(), value.getTo())
            );
        }
    }
    public void setValue(final String key, final Request value){
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, 4, TimeUnit.HOURS);
    }
}
