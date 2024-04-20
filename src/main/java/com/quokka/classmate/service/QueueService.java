package com.quokka.classmate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j(topic = "Redis Queue Service")
@Service
@RequiredArgsConstructor
public class QueueService {
    private final int FIRST_INDEX = 0;
    private final int LAST_INDEX = -1;
    private final RedisTemplate<String, String> redisTemplate;

    public void addQueue() {

    }

}
