package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.SubjectResponseDto;
import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // JUnit 5와 Mockito 연동
class SubjectServiceTest {

    @Mock // Mock 객체 생성
    private SubjectRepository subjectRepository;

    @InjectMocks // Mock 객체 레포지토리 주입
    private SubjectService subjectService;

    @Test
    @DisplayName("강의 과목 전체 조회 시, 누락되는 과목이 없어야 한다.")
    void findAll() {
        // given
        // Mock 데이터 담기용 리스트 생성
        List<Subject> mockSubjects = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Subject subject = new Subject("가짜과목 " + i, 30, 3, 3);
            mockSubjects.add(subject);
        }

        when(subjectRepository.saveAll(mockSubjects)).thenReturn(mockSubjects);

        // when
        List<SubjectResponseDto> result = subjectService.findAll();

        // then
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("강의 과목의 ID로 조회 시, 해당 과목의 정보가 조회돼야 한다.")
    void findById() {
        // given
        Long id = 1L;
        Subject mockSubject = new Subject("가짜과목", 30, 3, 3);
        when(subjectRepository.findById(id)).thenReturn(java.util.Optional.of(mockSubject));

        // when
        SubjectResponseDto result = subjectService.findById(id);

        // then
        assertThat(mockSubject.getName()).isEqualTo(result.getName());
        assertThat(mockSubject.getLimitCount()).isEqualTo(result.getLimitCount());
        assertThat(mockSubject.getTime()).isEqualTo(result.getTime());
        assertThat(mockSubject.getCredit()).isEqualTo(result.getCredit());
    }
}