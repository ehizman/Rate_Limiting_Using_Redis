package com.ehizman.mmr_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MmrApplication {

    public static void main(String[] args) {
        SpringApplication.run(MmrApplication.class, args);
    }

}
