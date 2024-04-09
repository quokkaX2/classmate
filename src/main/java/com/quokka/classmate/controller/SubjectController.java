package com.quokka.classmate.controller;

import com.quokka.classmate.domain.dto.SubjectResponseDto;
import com.quokka.classmate.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/")
    public String getAllSubjects(Model model) {
//        List<SubjectResponseDto> subjects = subjectService.findAll();
//        model.addAttribute("subjects", subjects);
//        model.addAttribute("currentPage", "courses");

        return "main";
    }

    @GetMapping("/api/search")
    public ResponseEntity<List<SubjectResponseDto>> getSubjectByInput(@RequestParam String input) {
        List<SubjectResponseDto> subjects = subjectService.findByKeyword(input);
//        model.addAttribute("subjects", subjects);
//        model.addAttribute("currentPage", "courses");

        return ResponseEntity.ok().body(subjects);
    }
}
