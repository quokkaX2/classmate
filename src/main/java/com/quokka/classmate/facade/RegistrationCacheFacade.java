package com.quokka.classmate.facade;

import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.repository.RedisRepository;
import com.quokka.classmate.service.RegisteredSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationCacheFacade {

    private final RedisRepository redisRepository;
    private final RegisteredSubjectService registeredSubjectService;

    public void registerByCache(Long subjectId, Long studentId) {

        // 레디스에 저장 되어있는 경우, 인원이 0이라면 바로 예외 처리
        if (redisRepository.hasLeftSeatsInRedis(subjectId) && !redisRepository.checkLeftSeatInRedis(subjectId)){
            throw new IllegalArgumentException("-- 신청 인원이 마감되었습니다. --");
        }

        // 실제 수강 신청 수행 - 비관적 락
        Subject subject = registeredSubjectService.registrationSubjectByPessimisticLock(subjectId, studentId);

        // 레디스에 남은 수강인원 저장
        redisRepository.saveCourseToRedis(subject);
    }

}
