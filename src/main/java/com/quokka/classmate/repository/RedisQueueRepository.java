package com.quokka.classmate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisQueueRepository {
    private final RedisTemplate<String, String> redisTemplate;
    public void addQueue(String key, String value, Long score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }
    public void removeQueue(String key, String value) {
        redisTemplate.opsForZSet().remove(key, value);
    }
}

