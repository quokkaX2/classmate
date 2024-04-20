package com.quokka.classmate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quokka.classmate.domain.dto.RedisQueueRequestDto;
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
    private final String ADD_QUEUE = "대기열 추가";
    private final String PROCESS = "수강신청 처리";
    private final ObjectMapper objectMapper;
    private final int FIRST_INDEX = 0;
    private final int LAST_INDEX = -1;
    private final int SIZE = 10;
    private final RedisTemplate<String, String> redisTemplate;
    private final RegisteredSubjectService registeredSubjectService;
    private final RegistrationCacheFacade registrationCacheFacade;
    private final RedisRepository repository;

    //레디스 대기열에 추가하는 메소드
    public void addQueue(Long subjectId, RedisQueueRequestDto requestDto) throws JsonProcessingException {
        checkCache(subjectId);
        String value = objectMapper.writeValueAsString(requestDto);
        Long score = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(ADD_QUEUE, value, score);
        log.info(requestDto.getStudentId() + "번 학생이 " + requestDto.getSubjectId() + "번 과목" + score + "에 대기열 추가함");
    }
    //이벤트 스케쥴러에서 주기적으로 실행되는 실제 신청 추가 메소드
    public void process() {
        Set<String> redisQueue = redisTemplate.opsForZSet().range(ADD_QUEUE, 0, SIZE);
        assert redisQueue != null;
        redisQueue.parallelStream().forEach(info -> {
            redisTemplate.opsForZSet().remove(ADD_QUEUE, info);
            try {
                log.info("process 작업 처리 시작");
                handleItem(info);
            } catch (JsonProcessingException e) {
                log.info("실패");
                throw new RuntimeException(e);
            }
        });
    }
    private void handleItem(String info) throws JsonProcessingException {
        RedisQueueRequestDto requestDto = objectMapper.readValue(info, RedisQueueRequestDto.class);
        Long studentId = requestDto.getStudentId();
        Long subjectId = requestDto.getSubjectId();
//      registrationCacheFacade.registerByCache(subjectId, studentId); //레디스 캐싱 적용 버전
        //registeredSubjectService.registrationSubjectByPessimisticLock(subjectId, studentId);
        try {
            registrationCacheFacade.registerByCache(subjectId, studentId);
//            registeredSubjectService.registrationSubjectByPessimisticLock(subjectId, studentId);
            log.info("수강 신청 완료");
        } catch (Exception e) {
            log.info("데이터베이스 락 대기 문제 발생", e);
            throw e; // 다시 예외를 던져 처리하도록 함
        }
    }
    private void checkCache(Long subjectId) {
        if(repository.hasLeftSeatsInRedis(subjectId)) {
            throw new IllegalArgumentException("-- 신청 인원이 마감되었습니다. --");
        }
    }
}
