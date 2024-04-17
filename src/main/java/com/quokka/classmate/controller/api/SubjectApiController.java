package com.quokka.classmate.controller.api;

import com.quokka.classmate.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SubjectApiController {

    private final SubjectService subjectService;


//    @GetMapping("/api/search")
//    public ResponseEntity<?> getSubjectByInput(@RequestParam String input) {
////        List<SubjectResponseDto> subjects = subjectService.findByKeyword(input);
////        model.addAttribute("subjects", subjects);
////        model.addAttribute("currentPage", "courses");
//
////            return subjectService.findByKeyword(input);
//            return subjectService.searchTitleByIndexing(input);
//    }

//    @GetMapping("/api/search")
//    public ResponseEntity<?> getSubjectsByInputAndCursor(@RequestParam String input,
//                                                         @RequestParam(required = false) Long cursor,
//                                                         @RequestParam(defaultValue = "10") int size) {
//        if (input.isEmpty()) {
//            throw new IllegalArgumentException("검색어를 입력해주세요.");
//        }
//        return ResponseEntity.ok(subjectService.searchByCursor(input, cursor, size));
//    }

    @GetMapping("/api/search")
    public ResponseEntity<?> getSubjectsByInputAndCursor(@RequestParam String input,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("검색어를 입력해주세요.");
        }
        return ResponseEntity.ok(subjectService.searchByCursorWithoutNative(input, null, page, size));
    }
}
