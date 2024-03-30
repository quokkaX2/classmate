package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.SubjectResponseDto;
import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.repository.SubjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    // 모든 과목 정보들 조회(과목 리스트 메인 페이지 접속)
    @Transactional(readOnly = true)
    public List<SubjectResponseDto> findAll() {
        return subjectRepository.findAll().stream().map(SubjectResponseDto::new).toList();
    }

    // 과목 키워드 검색
    @Transactional(readOnly = true)
    public List<SubjectResponseDto> findByKeyword(String input) {
        return subjectRepository.findByNameContaining(input)
                .stream().map(SubjectResponseDto::new).toList();
    }
}
