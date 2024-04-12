package com.quokka.classmate.service;

import com.quokka.classmate.domain.entity.RegisteredSubject;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.global.constant.GlobalVariables;
import com.quokka.classmate.global.security.UserDetailsImpl;
import com.quokka.classmate.repository.RegisteredSubjectRepository;
import com.quokka.classmate.repository.StudentRepository;
import com.quokka.classmate.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j // SLF4J 로깅 어노테이션
@Service
@RequiredArgsConstructor
public class SubjectLockFacade {
    private final RegisteredSubjectService registeredSubjectService;
    private final RedissonClient redissonClient;

    public void registrationSubject(Long subjectId, UserDetailsImpl userDetails) {
        String lockKey = "subjectLock:" + subjectId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            log.info("Trying to acquire lock for subjectId: {}", subjectId);
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                log.warn("Failed to acquire lock for subjectId: {}", subjectId);
                throw new IllegalArgumentException("Lock acquisition failed");
            }
            log.info("Lock acquired for subjectId: {}", subjectId);

            registeredSubjectService.registrationSubject(subjectId, userDetails);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while waiting for lock", e);
        } finally {
            lock.unlock(); // 작업 완료 후 분산 락 해제
            log.info("Lock released for subjectId: {}", subjectId);
        }
    }
}
