package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.StudentSignUpRequestDto;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.global.constant.GlobalVariables;
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


    // 회원가입
    public void signup(StudentSignUpRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<Student> checkUsername = studentRepository.findByEmail(email);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        Student student = new Student(requestDto.getEmail(), password, requestDto.getName(), 0);

        studentRepository.save(student);
    }

}
