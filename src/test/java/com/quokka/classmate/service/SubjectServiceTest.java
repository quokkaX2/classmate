package com.quokka.classmate.service;

import com.quokka.classmate.repository.SubjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

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

        // when

        // then
    }

    @Test
    @DisplayName("강의 과목의 ID로 조회 시, 해당 과목의 정보가 조회돼야 한다.")
    void findById() {
        // given

        // when

        // then
    }
}