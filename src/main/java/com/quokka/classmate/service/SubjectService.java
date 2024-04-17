package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.SubjectResponseDto;
import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.repository.SubjectRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);


    // 모든 과목 정보들 조회(과목 리스트 메인 페이지 접속)
    public List<SubjectResponseDto> findAll() {
        return subjectRepository.findAll().stream().map(
                subject -> new SubjectResponseDto(subject, subject.getClassTime())).toList();
    }

    // 과목 키워드 검색
    public ResponseEntity<?> findByKeyword(String input) {
        if (input.isEmpty()) {
            throw new NullPointerException("과목은 한 글자 이상 입력해주세요");
        }
        List<SubjectResponseDto> subjects =  subjectRepository.findByTitleContaining(input)
                .stream().map(
                        subject ->
                                new SubjectResponseDto(subject, subject.getClassTime())).toList();

        if (subjects.isEmpty()) {
            throw new IllegalArgumentException("과목이 존재하지 않습니다.");
        }

        return ResponseEntity.ok().body(subjects);
    }

    public ResponseEntity<?> searchTitleByIndexing(String input) {
        if (input.isEmpty()) {
            throw new NullPointerException("과목은 한 글자 이상 입력해주세요");
        }

        List<SubjectResponseDto> subjects = subjectRepository.searchByTitleFullText(input) // 풀텍스트 검색 메소드 호출
                .stream()
                .map(subject -> new SubjectResponseDto(subject, subject.getClassTime()))
                .toList();

        if (subjects.isEmpty()) {
            throw new IllegalArgumentException("과목이 존재하지 않습니다.");
        }

        return ResponseEntity.ok().body(subjects);
    }

    public List<SubjectResponseDto> searchByCursor(String input, Long cursor, int size) {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록
        List<Subject> subjects = subjectRepository.findByTitleWithCursor(input, cursor, size);
        List<SubjectResponseDto> response = subjects
                .stream()
                .map(subject -> new SubjectResponseDto(subject, subject.getClassTime()))
                .toList();
        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        logger.info("searchByCursor executed in {} ms", endTime - startTime); // 실행 시간 로깅

        return response;
    }

    public List<SubjectResponseDto> searchByCursorWithoutNative(String input, Long cursor, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        long startTime = System.currentTimeMillis();
        Page<Subject> subjects = subjectRepository.findByTitleContainingWithCursor(input, cursor, pageable);
        long endTime = System.currentTimeMillis();
        logger.info("searchByCursor executed in {} ms", endTime - startTime);

        return subjects.getContent()
                .stream()
                .map(subject -> new SubjectResponseDto(subject, subject.getClassTime()))
                .collect(Collectors.toList());
    }
}
