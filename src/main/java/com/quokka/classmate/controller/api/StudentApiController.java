package com.quokka.classmate.controller.api;

import com.quokka.classmate.domain.dto.StudentSignUpRequestDto;
import com.quokka.classmate.global.exception.ApiResponseDto;
import com.quokka.classmate.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentApiController {

    private final StudentService studentService;

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody StudentSignUpRequestDto requestDto) {
            studentService.signup(requestDto);
            return ResponseEntity.ok(new ApiResponseDto("회원가입이 성공되었습니다."));

    }

}
