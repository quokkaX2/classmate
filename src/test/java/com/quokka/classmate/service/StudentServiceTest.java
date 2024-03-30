package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.StudentRequestDto;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void signup_success() {
        StudentRequestDto requestDto = new StudentRequestDto(
                "text@example.com",
                "securePassword",
                "Test Student Name"
        );

        when(studentRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("hashedPassword");
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArguments());

        verify(studentRepository, times(1)).save(any(Student.class));

    }

}
