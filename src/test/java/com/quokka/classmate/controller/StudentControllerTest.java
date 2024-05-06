package com.quokka.classmate.controller;

import com.quokka.classmate.domain.controller.routing.StudentController;
import com.quokka.classmate.domain.dto.StudentSignUpRequestDto;
import com.quokka.classmate.domain.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    @WithMockUser
    @DisplayName("회원가입 성공 경우")
    public void signup_success() throws Exception {
        StudentSignUpRequestDto requestDto = new StudentSignUpRequestDto(
                "test@example.com",
                "securePassword",
                "Test User"
        );
        doNothing().when(studentService).signup(any(StudentSignUpRequestDto.class));

        mockMvc.perform(post("/signup")
                        .param("email", requestDto.getEmail())
                        .param("password", requestDto.getPassword())
                        .param("name", requestDto.getName())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 실패 경우 - 이메일 중복")
    public void signup_failure_duplicateEmail() throws Exception {
        String duplicateEmailMessage = "중복된 사용자가 존재합니다.";
        doThrow(new IllegalArgumentException(duplicateEmailMessage))
                .when(studentService).signup(any(StudentSignUpRequestDto.class));

        mockMvc.perform(post("/signup")
                        .param("email", "duplicate@example.com")
                        .param("password", "password123")
                        .param("name", "Duplicate User")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "회원가입 실패: 중복된 사용자가 존재합니다."));
    }
}
