package com.quokka.classmate.controller.api;

import com.quokka.classmate.service.SubjectService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SubjectApiController {

    private final SubjectService subjectService;
    private static final Logger logger = LoggerFactory.getLogger(SubjectApiController.class);


    @GetMapping("/api/search")
    public ResponseEntity<?> getSubjectByInput(@RequestParam String input) {
//        List<SubjectResponseDto> subjects = subjectService.findByKeyword(input);
//        model.addAttribute("subjects", subjects);
//        model.addAttribute("currentPage", "courses");

//        return subjectService.findByKeyword(input);
        long startTime = System.currentTimeMillis(); // 검색 시작 시간 측정
        try {
            return subjectService.findByKeyword(input);
//            return subjectService.searchTitle(input);
        } finally {
            long endTime = System.currentTimeMillis(); // 검색 종료 시간 측정
            logger.info("검색 처리 시간: {} ms", endTime - startTime); // 로깅
        }
    }
}
