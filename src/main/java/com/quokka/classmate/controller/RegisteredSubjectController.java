package com.quokka.classmate.controller;

import com.quokka.classmate.domain.dto.CartResponseDto;
import com.quokka.classmate.global.security.UserDetailsImpl;
import com.quokka.classmate.service.RegisteredSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RegisteredSubjectController {

    private final RegisteredSubjectService registeredSubjectService;

    // 수강 과목 장바구니
    @GetMapping("/cart")
    public String getAllCarts(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CartResponseDto> carts = registeredSubjectService.findAll(userDetails.getUser());
        model.addAttribute("carts", carts);
        model.addAttribute("currentPage", "cart");
        return "cart";
    }

    // 수강 과목 장바구니에 추가
    @PostMapping("/api/cart/{subjectId}")
    public ResponseEntity<String> saveRegisteredSubject(
            @PathVariable Long subjectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return registeredSubjectService.createRegisteredSubject(subjectId, userDetails);
    }

    // 수강 과목 장바구니에서 삭제
    // html 파일에서 타임리프 버튼 추가 필요
    // 과목 기반 경로변수 vs 등록과목 기반 경로변수 -> 비교 필요
    @DeleteMapping("/api/cart/{subjectId}")
    public ResponseEntity<String> deleteRegisteredSubject(
            @PathVariable Long subjectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return registeredSubjectService.deleteRegisteredSubject(subjectId, userDetails);
    }

    // 장바구니에 담은 과목 --> '수강 신청'
    @PostMapping("/api/register/{subjectId}")
    public ResponseEntity<String> registrationSubject(
            @PathVariable Long subjectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return registeredSubjectService.registrationSubject(subjectId, userDetails);
    }
}
