package com.quokka.classmate.controller.api;

import com.quokka.classmate.global.exception.ApiResponseDto;
import com.quokka.classmate.global.security.UserDetailsImpl;
import com.quokka.classmate.service.RegisteredSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisteredSubjectApiController {

    private final RegisteredSubjectService registeredSubjectService;

    // 장바구니에 담은 과목 --> '수강 신청'
    @PostMapping("/api/register/{subjectId}")
    public ResponseEntity<?> registrationSubject(
            @PathVariable Long subjectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        registeredSubjectService.registrationSubject(subjectId, userDetails);

        return ResponseEntity.ok(new ApiResponseDto("수강 신청이 완료되었습니다."));
    }
}
