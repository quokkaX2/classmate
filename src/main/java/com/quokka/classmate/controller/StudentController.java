package com.quokka.classmate.controller;

import com.quokka.classmate.domain.dto.StudentSignUpRequestDto;
import com.quokka.classmate.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j(topic = "StudentController")
@Controller
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String registerPage() {
        return "signup";
    }
}
