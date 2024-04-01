package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.CartResponseDto;
import com.quokka.classmate.domain.entity.RegisteredSubject;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.repository.RegisteredSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisteredSubjectService {

    private final RegisteredSubjectRepository registeredSubjectRepository;

    // 장바구니에 담은 과목 전체 조회
    public List<CartResponseDto> findAll(Student student) {
        List<RegisteredSubject> registeredSubjects = registeredSubjectRepository.findAllByStudentId(student.getId());
        return registeredSubjects.stream()
                .map(CartResponseDto::new)
                .toList();
    }
}
