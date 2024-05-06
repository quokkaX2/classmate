package com.quokka.classmate.global.facade;

import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.domain.repository.RedisRepository;
import com.quokka.classmate.domain.service.RegisteredSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationCacheFacade {

    private final RedisRepository redisRepository;
    private final RegisteredSubjectService registeredSubjectService;

    public void registerByCache(Long subjectId, Long studentId) {

        // 레디스에 저장 되어있는 경우, 인원이 0이라면 바로 예외 처리
        if (redisRepository.hasLeftSeatsInRedis(subjectId)){
            throw new IllegalArgumentException("-- 신청 인원이 마감되었습니다. --");
        }

        // 실제 수강 신청 수행 - 비관적 락
        Subject subject = registeredSubjectService.registrationSubjectByPessimisticLock(subjectId, studentId);
        checkCount(subject);
    }
    // 레디스에 0명일 경우 저장
    private void checkCount(Subject subject) {

        if (subject.getLimitCount()==0){
            redisRepository.saveCourseToRedis(subject);
        }
    }
}
