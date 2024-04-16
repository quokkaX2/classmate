package com.quokka.classmate.facade;

import com.quokka.classmate.service.RegisteredSubjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@AllArgsConstructor
public class RedissonLockFacade {

    private RedissonClient redissonClient;
    private RegisteredSubjectService registeredSubjectService;

    public void registerSubject(Long subjectId, Long studentId) {

        RLock lock = redissonClient.getLock(subjectId.toString());

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                log.error("학생 {}의 해당 {} 과목에 대한 잠금 획득 실패", studentId, subjectId);
                return;
            }

            // log.info("학생 {}의 해당 {} 과목에 대한 잠금 획득 성공", studentId, subjectId);
            registeredSubjectService.registrationSubject(subjectId, studentId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            // log.info("학생 {}의 해당 {} 과목에 대한 잠금 해제", studentId, subjectId);
        }
    }

}
