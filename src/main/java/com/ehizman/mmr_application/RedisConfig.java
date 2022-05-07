package com.ehizman.mmr_application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.net.URI;
import java.net.URISyntaxException;


@Configuration
@Slf4j
public class RedisConfig {
    @Autowired
    private ConfigurableEnvironment env;

    @Bean
    public JedisPool jedisPool() {
        String[] profiles = env.getActiveProfiles();
        if (profiles[0].equals("prod")){
            String redisURL = System.getenv("REDISTOGO_URL");
            URI redisURI = null;
            try {
                redisURI = new URI(redisURL);
                return new JedisPool(new JedisPoolConfig(),
                        redisURI.getHost(),
                        redisURI.getPort(),
                        Protocol.DEFAULT_TIMEOUT,
                        redisURI.getUserInfo().split(":",2)[1]);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return new JedisPool();
    }
}
