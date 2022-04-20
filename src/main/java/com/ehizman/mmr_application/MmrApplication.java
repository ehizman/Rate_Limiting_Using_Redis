package com.ehizman.mmr_application;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;

@EnableCaching
@SpringBootApplication
@Slf4j
public class MmrApplication {
    public static void main(String[] args) {
        SpringApplication.run(MmrApplication.class, args);
        connect();
    }

    public static RedisCommands<String, String> connect(){
        RedisURI redisURI = RedisURI.create(System.getenv("REDIS_ENDPOINT_URI"));
        log.info("RedisURI --> {}", redisURI);
        redisURI.setVerifyPeer(false);

        RedisClient redisClient = RedisClient.create(redisURI);
        return redisClient.connect().sync();
    }
}
