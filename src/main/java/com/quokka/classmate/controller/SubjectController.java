package com.quokka.classmate.controller;

import com.quokka.classmate.domain.dto.SubjectResponseDto;
import com.quokka.classmate.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/")
    public String getAllSubjects(Model model) {
        List<SubjectResponseDto> subjects = subjectService.findAll();
        model.addAttribute("subjects", subjects);

        return "main";
    }

    @GetMapping("/api/search")
    public String getSubjectByInput(@RequestParam String input, Model model) {
        List<SubjectResponseDto> subjects = subjectService.findByKeyword(input);
        model.addAttribute("subjects", subjects);

        return "main";
    }
}
