package com.quokka.classmate.repository;

import com.quokka.classmate.domain.entity.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private static final String SUBJECT_KEY_PREFIX = "subject"; // 과목의 redis key prefix 설정

    private final RedisTemplate<String, Integer> redisTemplate;

    // redis 서버확인
    public boolean isRedisDown() {
        try {
            redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    // 강의 잔여 자리 수를 Redis에 저장
    public void saveCourseToRedis(Subject subject){
        String key = SUBJECT_KEY_PREFIX + subject.getId();
        redisTemplate.opsForValue().set(key, subject.getLimitCount());
    }

    // key로 redis에 캐시가 있는지 조회하고 Boolean 반환
    public Boolean hasLeftSeatsInRedis(Long subjectId){
        String key = SUBJECT_KEY_PREFIX + subjectId;
        return redisTemplate.hasKey(key);
    }

    // 수강 신청 가능한지 확인
    public Boolean checkLeftSeatInRedis(Long subjectId){
        String key = SUBJECT_KEY_PREFIX + subjectId;
        if (redisTemplate.opsForValue().get(key) <= 0) {
            return false;
        } else {
            return true;
        }
    }


    // Redis랑 DB 정합성 검사
    public void refreshLeftSeats(){
//        log.error("redis 서버가 닫혔습니다.");
//        List<Registration> registrations = registrationRepository.findAll();
//        Set<Long> courseIdList = null;
//        registrations.forEach(registration -> {
//            courseIdList.add(registration.getCourse().getId());
//        });
//
//        courseIdList.forEach(courseId -> {
//            Course course = courseRepository.findById(courseId).orElseThrow(
//                    () -> new RequiredFieldException(COURSE_NOT_FOUND)
//            );
//            Long accurateReserveSeats = registrationRepository.countByCourseId(courseId);
//            if (accurateReserveSeats == null) {
//                throw new RequiredFieldException(REGISTRATION_NOT_FOUND);
//            }
//            Long accurateLeftSeats = course.getLimitation() - accurateReserveSeats;
//            String key = "c" + courseId;
//            course.setCurrent(accurateLeftSeats);
//            redisTemplate.opsForValue().set(key, accurateLeftSeats);
//        });
    }

}
