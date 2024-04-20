package com.quokka.classmate.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quokka.classmate.domain.dto.RedisQueueRequestDto;
import com.quokka.classmate.facade.RedissonLockFacade;
import com.quokka.classmate.facade.RegistrationCacheFacade;
import com.quokka.classmate.global.exception.ApiResponseDto;
import com.quokka.classmate.global.security.UserDetailsImpl;
import com.quokka.classmate.service.QueueService;
import com.quokka.classmate.service.RegisteredSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisteredSubjectApiController {

    private final RegisteredSubjectService registeredSubjectService;
    private final RedissonLockFacade redissonLockFacade;
    private final RegistrationCacheFacade registrationCacheFacade; // 수강 신청 가능 잔여 수 redis 적용
    private final QueueService queueService; //레디스 대기열 관련 서비스

    // 수강 과목 장바구니에 추가
    @PostMapping("/api/cart/{subjectId}")
    public ResponseEntity<?> saveRegisteredSubject(
            @PathVariable Long subjectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        registeredSubjectService.createRegisteredSubject(subjectId, userDetails.getUser().getId());

        return ResponseEntity.ok(new ApiResponseDto("수강 과목이 장바구니에 추가 되었습니다."));
    }

    // 수강 과목 장바구니에서 삭제
    // html 파일에서 타임리프 버튼 추가 필요
    // 과목 기반 경로변수 vs 등록과목 기반 경로변수 -> 비교 필요
    @DeleteMapping("/api/cart/{subjectId}")
    public ResponseEntity<?> deleteRegisteredSubject(
            @PathVariable Long subjectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        registeredSubjectService.deleteRegisteredSubject(subjectId, userDetails.getUser().getId());

        return ResponseEntity.ok(new ApiResponseDto("장바구니에서 삭제 되었습니다."));
    }

    // 장바구니에 담은 과목 --> '수강 신청'
//    @PostMapping("/api/register/{subjectId}")
//    public ResponseEntity<?> registrationSubject(
//            @PathVariable Long subjectId,
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
////        registeredSubjectService.registrationSubject(subjectId, userDetails.getUser().getId());
//        registeredSubjectService.registrationSubjectByPessimisticLock(subjectId, userDetails.getUser().getId());
////        redissonLockFacade.registerSubject(subjectId, userDetails.getUser().getId());
////        registrationCacheFacade.registerByCache(subjectId, userDetails.getUser().getId()); // 수강신청 인원 redis 적용
//
//        return ResponseEntity.ok(new ApiResponseDto("수강 신청이 완료되었습니다."));
//    }

    // 수강 신청 시 바로 서비스에서 DB에 접근하여 처리하는게 아닌 레디스 대기열에 추가
    @PostMapping("/api/register/{subjectId}")
    public ResponseEntity<?> registrationSubject(
            @PathVariable Long subjectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {
        RedisQueueRequestDto requestDto = new RedisQueueRequestDto(userDetails.getUser().getId(), subjectId);
        queueService.addQueue(subjectId, requestDto);
        return ResponseEntity.ok(new ApiResponseDto("대기열 추가에 성공 했습니다."));
    }
}
