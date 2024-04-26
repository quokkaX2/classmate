package com.quokka.classmate.controller.api;

import com.quokka.classmate.domain.dto.PaginationResponseDto;
import com.quokka.classmate.domain.dto.ResponseDto;
import com.quokka.classmate.domain.dto.SubjectResponseDto;
import com.quokka.classmate.service.ElasticSubjectService;
import com.quokka.classmate.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/search")
public class SubjectApiController {

    private final SubjectService subjectService;

    private final ElasticSubjectService elasticSubjectService;

    @GetMapping(params = "title")
    public ResponseDto<PaginationResponseDto<SubjectResponseDto>> searchSubjectByTitlePagination(
            @RequestParam String title,
            @PageableDefault Pageable pageable)
    {

        Page<SubjectResponseDto> page = elasticSubjectService.searchSubjectByTitle(pageable, title);


        return ResponseDto.<PaginationResponseDto<SubjectResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(PaginationResponseDto.<SubjectResponseDto>builder()
                        .dataList(page.getContent())
                        .totalDataCount(page.getTotalElements())
                        .currentPage(page.getNumber())
                        .totalPage(page.getTotalPages())
                        .build())
                .build();

    }

//        @GetMapping("/api/search")
//    public ResponseEntity<?> getSubjectsByInputAndCursor(@RequestParam String input,
//                                                         @RequestParam(required = false) Long cursor,
//                                                         @RequestParam(defaultValue = "10") int size) {
//        if (input.isEmpty()) {
//            throw new IllegalArgumentException("검색어를 입력해주세요.");
//        }
//        return ResponseEntity.ok(subjectService.searchByCursor(input, cursor, size));
//    }
//    @GetMapping("/api/search")
//    public ResponseEntity<?> getSubjectsByInputAndCursor(@RequestParam String input,
//                                                         @RequestParam(defaultValue = "0") int page,
//                                                         @RequestParam(defaultValue = "10") int size) {
//
////        List<SubjectResponseDto> subjects = subjectService.findByKeyword(input);
////        model.addAttribute("subjects", subjects);
////        model.addAttribute("currentPage", "courses");
//        if (input.isEmpty()) {
//            throw new IllegalArgumentException("검색어를 입력해주세요.");
//        }
//        //return subjectService.searchTitleByIndexing(input, page-1, size);
////        return subjectService.findByKeyword(input);
//        return ResponseEntity.ok(subjectService.searchByCursorWithoutNative(input, null, page, size));
//
//    }
}
