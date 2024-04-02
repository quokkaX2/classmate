//package com.quokka.classmate.controller;
//
//import com.quokka.classmate.domain.entity.Student;
//import com.quokka.classmate.global.security.UserDetailsImpl;
//import com.quokka.classmate.service.RegisteredSubjectService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//
//@WebMvcTest(SubjectController.class)
//@MockBean(JpaMetamodelMappingContext.class)
//class RegisteredSubjectControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc; // HTTP 호출 담당
//
//    @MockBean
//    private RegisteredSubjectService registeredSubjectService;
//
//
//    @Test
//    @WithMockUser
//    @DisplayName("장바구니에 강의를 등록해둘 수 있다.")
//    void saveRegisteredSubject() throws Exception {
//        // given
//        Long mockSubjectId = 1L;
//        Student mockStudent = new Student();
//        UserDetailsImpl userDetails = new UserDetailsImpl(mockStudent);
//
//        // when, then
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/registered-subjects")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"subjectId\":1}")
//                        .with(user(userDetails))) // 가상의 사용자 정보를 요청에 추가
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//}