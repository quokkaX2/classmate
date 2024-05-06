package com.quokka.classmate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quokka.classmate.domain.dto.RedisQueueRequestDto;
import com.quokka.classmate.domain.redisEnum.Redis;
import com.quokka.classmate.facade.RegistrationCacheFacade;
import com.quokka.classmate.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j(topic = "Redis Queue Service")
@Service
@RequiredArgsConstructor
public class QueueService {
    private final int SIZE = 100;

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final RegistrationCacheFacade registrationCacheFacade;
    private final RedisRepository repository;

    //레디스 대기열에 추가하는 메소드
    public void addQueue(Long subjectId, RedisQueueRequestDto requestDto) throws JsonProcessingException {
        checkCache(subjectId);
        String value = objectMapper.writeValueAsString(requestDto);
        Long score = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(Redis.ADD_QUEUE.getCategory(), value, score);
    }
    //이벤트 스케쥴러에서 주기적으로 실행되는 실제 신청 추가 메소드
    public void process() {
        Set<String> redisQueue = redisTemplate.opsForZSet().range(Redis.ADD_QUEUE.getCategory(), 0, SIZE);
        assert redisQueue != null;

        redisQueue.parallelStream().forEach(info -> {
            try {
                redisTemplate.opsForZSet().remove(Redis.ADD_QUEUE.getCategory(), info);
                RedisQueueRequestDto requestDto = objectMapper.readValue(info, RedisQueueRequestDto.class);
                handleItem(requestDto);
                redisTemplate.opsForZSet().add(Redis.SUCCESS.getCategory(), info, System.currentTimeMillis());
            } catch (Exception e) {
                // Handle the exception here without rethrowing it
                redisTemplate.opsForZSet().add(Redis.FAIL.getCategory(), info, System.currentTimeMillis());
                log.error("An error occurred while processing queue item: {}", e.getMessage(), e);
            }
        });
    }
    //대기열 안에서 취소
    public void removeQueue(RedisQueueRequestDto requestDto){
        try {
            String value = objectMapper.writeValueAsString(requestDto);
            redisTemplate.opsForZSet().remove(Redis.ADD_QUEUE.getCategory(), value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    //대기열 내용 실제 처리
    private void handleItem(RedisQueueRequestDto requestDto) throws JsonProcessingException {
        Long studentId = requestDto.getStudentId();
        Long subjectId = requestDto.getSubjectId();
        registrationCacheFacade.registerByCache(subjectId, studentId);
    }
    //레디스 내부에서 캐쉬 체크 후 미리 정리
    private void checkCache(Long subjectId) {
        if (repository.hasLeftSeatsInRedis(subjectId)) {
            throw new IllegalArgumentException(Redis.CLOSED.getCategory());
        }
    }
}
