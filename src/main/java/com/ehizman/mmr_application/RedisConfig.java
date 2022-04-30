package com.ehizman.mmr_application;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.pivotal.cfenv.core.CfEnv;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;

@Configuration
@Slf4j
public class RedisConfig {
    @Bean
    public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
        return clientConfigurationBuilder -> {
            if (clientConfigurationBuilder.build().isUseSsl()) {
                clientConfigurationBuilder.useSsl().disablePeerVerification();
            }
        };
    }
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String endpointUrl = System.getenv("REDIS_ENDPOINT_URL");
        if (endpointUrl == null) {
            endpointUrl = "127.0.0.1:6379";
        }
        String password = System.getenv("REDIS_PASSWORD");

        String[] urlParts = endpointUrl.split(":");

        String host = urlParts[0];
        String port = "6379";

        if (urlParts.length > 1) {
            port = urlParts[1];
        }

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, Integer.parseInt(port));

        System.out.printf("Connecting to %s:%s with password: %s%n", host, port, password);

        if (password != null) {
            config.setPassword(password);
        }
        return new LettuceConnectionFactory(config);

    }

    //Creating RedisTemplate for Entity 'PhoneNumber'
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer( new StringRedisSerializer() );
        template.setHashValueSerializer( new GenericToStringSerializer<>( Object.class ) );
        template.setValueSerializer( new GenericToStringSerializer<>( Object.class ));
        return template;
    }
}
