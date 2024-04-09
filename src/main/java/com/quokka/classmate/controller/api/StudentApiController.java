package com.quokka.classmate.controller.api;

import com.quokka.classmate.domain.dto.StudentSignUpRequestDto;
import com.quokka.classmate.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudentApiController {

    private final StudentService studentService;

    // 회원가입 API
    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody StudentSignUpRequestDto requestDto) {
        return studentService.signup(requestDto);

    }

}
