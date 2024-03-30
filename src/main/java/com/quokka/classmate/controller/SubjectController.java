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

    @GetMapping
    public String getAllSubjects(Model model) {
        List<SubjectResponseDto> subjects = subjectService.findAll();
        model.addAttribute("subjects", subjects);

        return null; // 추후 html이 작성되면 타임리프 문법 적용
    }

//    @GetMapping("/api/search")
//    public String getSubject(@RequestParam Long subjectId, Model model) {
//        SubjectResponseDto subject = subjectService.findById(subjectId);
//        model.addAttribute("subject", subject);
//
//        return null; // 추후 html이 작성되면 타임리프 문법 적용... 인데 이거 검색이 좀 잘못된듯?
//    }

    @GetMapping("/api/search")
    public String getSubjectByInput(@RequestParam String input, Model model) {
        List<SubjectResponseDto> subjects = subjectService.findByKeyword(input);
        model.addAttribute("subjects", subjects);

        return null; // 추후 html이 작성되면 타임리프 문법 적용
    }
}
