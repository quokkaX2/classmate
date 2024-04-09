package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.StudentSignUpRequestDto;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.global.constant.GlobalVariables;
import com.quokka.classmate.global.exception.ApiResponseDto;
import com.quokka.classmate.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입
    public ResponseEntity<?> signup(StudentSignUpRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<Student> existingStudent = studentRepository.findByEmail(email);
        if (existingStudent.isPresent()) {
           throw new IllegalArgumentException("Error: 중복된 사용자가 존재합니다.");
        }

        Student newStudent = new Student(email, password, requestDto.getName(), 0);
        studentRepository.save(newStudent);

        return ResponseEntity.ok(new ApiResponseDto("회원가입이 성공되었습니다."));
    }

}
