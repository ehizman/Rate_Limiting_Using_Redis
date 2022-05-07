package com.ehizman.mmr_application.services;

import com.ehizman.mmr_application.exceptions.APIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
@Slf4j
public class RedisUtility {
    @Autowired
    JedisPool jedisPool;

    public String getValue( final String key) throws APIException {
        try(Jedis jedis = jedisPool.getResource()){
            log.info("Key --> {}", jedis.get(key));
            return jedis.get(key);
        }

    }
    //changed redis config
    public void setValue(final String key, final String message){
        try(Jedis jedis = jedisPool.getResource()){
            jedis.set(key, message);
            jedis.expire(key, Long.valueOf(4*60*60));
        }
    }
}
