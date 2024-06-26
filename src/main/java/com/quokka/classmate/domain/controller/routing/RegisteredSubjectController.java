package com.quokka.classmate.domain.controller.routing;

import com.quokka.classmate.domain.dto.CartResponseDto;
import com.quokka.classmate.global.security.UserDetailsImpl;
import com.quokka.classmate.domain.service.RegisteredSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/profile")
    public String getStudentProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<CartResponseDto> registered = registeredSubjectService.getRegisteredSubjects(userDetails.getUser());
        model.addAttribute("registered", registered);
        model.addAttribute("student", userDetails.getUser());
        model.addAttribute("currentPage", "profile");
        return "profile";
    }
}
