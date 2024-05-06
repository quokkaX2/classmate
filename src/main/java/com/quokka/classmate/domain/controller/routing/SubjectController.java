package com.quokka.classmate.domain.controller.routing;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class SubjectController {

//    private final SubjectService subjectService;

    @GetMapping("/")
    public String getAllSubjects(Model model) {
//        List<SubjectResponseDto> subjects = subjectService.findAll();
//        model.addAttribute("subjects", subjects);
//        model.addAttribute("currentPage", "courses");

        return "main";
    }
}
