package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.StudentRequestDto;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    private final int MAX_CREDIT = 20;

    // 수강 신청 가능 여부를 확인하는 메서드
    public boolean canRegister(Long studentId, Integer subjectCredit) {
        Student student = studentRepository.findById(studentId).orElseThrow(() ->
                new IllegalArgumentException("유효하지 않은 학생 ID입니다."));

        int totalCredit = student.getCurrentCredit() + subjectCredit;

        return totalCredit <= MAX_CREDIT;
    }

    // 회원가입
    public void signup(StudentRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<Student> checkUsername = studentRepository.findByEmail(email);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        Student student = new Student();
        student.setEmail(requestDto.getEmail());
        student.setPassword(password);
        student.setName(requestDto.getName());
        student.setCurrentCredit(0);

        studentRepository.save(student);

    }

}
