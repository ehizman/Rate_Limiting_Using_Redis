package com.ehizman.mmr_application;

import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;

@EnableCaching
@SpringBootApplication
public class MmrApplication {
    @Autowired
    StatefulRedisConnection<String, String> statefulRedisConnection;
    public static void main(String[] args) {
        SpringApplication.run(MmrApplication.class, args);
    }

    @PostConstruct
    public void connectToRedis(){
        statefulRedisConnection.sync();
    }
}
