package com.quokka.classmate.controller;

import com.quokka.classmate.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

}
