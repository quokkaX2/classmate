package com.quokka.classmate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j(topic = "StudentController")
@Controller
@RequiredArgsConstructor
public class StudentController {

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
