package com.quokka.classmate.global.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;

@Configuration
@EnableCaching
public class RedisConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedissonClient redissonClient() {
        try {
            log.info("Connecting to Redis");
            Config config = new Config();
            config.useSingleServer().setAddress("redis://127.0.0.1:6379");
            RedissonClient client = Redisson.create(config);
            log.info("Successfully connected to Redis");
            return client;
        } catch (Exception e) {
            log.error("Failed to connect to Redis", e);
            throw e; // Re-throw to prevent application from starting without Redis
        }
    }
}