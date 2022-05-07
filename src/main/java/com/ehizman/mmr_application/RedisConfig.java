package com.ehizman.mmr_application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;
import java.net.URISyntaxException;


@Configuration
@Slf4j
public class RedisConfig {
    @Autowired
    private ConfigurableEnvironment env;
    JedisConnectionFactory jedisConnectionFactory() {
        String[] profiles = env.getActiveProfiles();
        if (profiles[0].equals("prod")){
            try {
                String redisURL = System.getenv("REDISTOGO_URL");
                String password = redisURL.substring(redisURL.indexOf(":", redisURL.indexOf(":"))+1, redisURL.indexOf("@"));
                URI redisURI = new URI(redisURL);
                RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(
                        redisURI.getHost(), redisURI.getPort()
                );
                standaloneConfiguration.setPassword(password);
                JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(standaloneConfiguration);
                jedisConnectionFactory.getPoolConfig().setMaxIdle(30);
                jedisConnectionFactory.getPoolConfig().setMinIdle(10);
                return jedisConnectionFactory;
            } catch (URISyntaxException e) {
                throw new RuntimeException("Redis couldn't be configured from URL in REDISTOGO_URL env var:"+
                        System.getenv("REDISTOGO_URL"));
            }
        }
        return new JedisConnectionFactory();
    }

    //Creating RedisTemplate for Entity 'PhoneNumber'
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer( new StringRedisSerializer() );
        template.setHashValueSerializer( new GenericToStringSerializer<>( Object.class ) );
        template.setValueSerializer( new GenericToStringSerializer<>( Object.class ));
        return template;
    }
}
