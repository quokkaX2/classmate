package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.SubjectResponseDto;
import com.quokka.classmate.global.exception.ApiResponseDto;
import com.quokka.classmate.repository.SubjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class SubjectService {

    private final SubjectRepository subjectRepository;

    // 모든 과목 정보들 조회(과목 리스트 메인 페이지 접속)
    public List<SubjectResponseDto> findAll() {
        return subjectRepository.findAll().stream().map(
                subject -> new SubjectResponseDto(subject, subject.getClassTime())).toList();
    }

    // 과목 키워드 검색
    public ResponseEntity<?> findByKeyword(String input) {
        if (input.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponseDto("과목은 한 글자 이상 입력해주세요"));
        }

        List<SubjectResponseDto> subjects =  subjectRepository.findByTitleContaining(input)
                .stream().map(
                        subject ->
                                new SubjectResponseDto(subject, subject.getClassTime())).toList();

        if (subjects.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponseDto("과목이 존재하지 않습니다."));
        }

        return ResponseEntity.ok().body(subjects);
    }
}
