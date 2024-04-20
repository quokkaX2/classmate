package com.quokka.classmate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quokka.classmate.domain.dto.RedisQueueRequestDto;
import com.quokka.classmate.domain.entity.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j(topic = "Redis Queue Service")
@Service
@RequiredArgsConstructor
public class QueueService {
    private final String ADD_QUEUE = "대기열 추가";
    private final String PROCESS = "수강신청 처리";
    private final ObjectMapper objectMapper;
    private final int FIRST_INDEX = 0;
    private final int LAST_INDEX = -1;
    private final RedisTemplate<String, String> redisTemplate;

    public void addQueue(RedisQueueRequestDto requestDto) throws JsonProcessingException {
        String value = objectMapper.writeValueAsString(requestDto);
        Long score = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(ADD_QUEUE, value, score);
        log.info(requestDto.getStudentId() + "번 학생이 " + requestDto.getSubjectId() + "번 과목" + score + "에 대기열 추가함");
    }

    public void process() {

    }
}
