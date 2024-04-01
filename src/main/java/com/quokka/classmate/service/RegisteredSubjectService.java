package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.CartResponseDto;
import com.quokka.classmate.domain.entity.RegisteredSubject;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.global.security.UserDetailsImpl;
import com.quokka.classmate.repository.RegisteredSubjectRepository;
import com.quokka.classmate.repository.StudentRepository;
import com.quokka.classmate.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegisteredSubjectService {

    private final RegisteredSubjectRepository registeredSubjectRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;

    // 장바구니에 담은 과목 전체 조회
    public List<CartResponseDto> findAll(Student student) {
        List<RegisteredSubject> registeredSubjects = registeredSubjectRepository.findAllByStudentId(student.getId());
        return registeredSubjects.stream()
                .map(CartResponseDto::new)
                .toList();
    }

    // 장바구니에 강의 담기(코드 최적화 x)
    @Transactional
    public ResponseEntity<String> createRegisterSubject(
            Long subjectId, UserDetailsImpl userDetails) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new IllegalArgumentException("추가하려는 강의가 존재하지 않습니다.")
        );

        Student student = studentRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원 정보입니다.")
        );

        registeredSubjectRepository.save(new RegisteredSubject());

        return null;
    }
}
