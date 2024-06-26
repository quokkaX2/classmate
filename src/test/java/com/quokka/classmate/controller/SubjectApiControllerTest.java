package com.quokka.classmate.controller;

import com.quokka.classmate.domain.controller.api.SubjectApiController;
import com.quokka.classmate.domain.dto.SubjectResponseDto;
import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.domain.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(SubjectApiController.class)
@MockBean(JpaMetamodelMappingContext.class)
class SubjectApiControllerTest {

    @Autowired
    private MockMvc mockMvc; // HTTP 호출 담당

    @MockBean
    private SubjectService subjectService;

    private List<SubjectResponseDto> mockSubjectResponses;

    @BeforeEach
    void setUp() {
        // given
        mockSubjectResponses = new ArrayList<>();
        List<Subject> mockSubjects = new ArrayList<>();

        mockSubjects.add(new Subject("과목1 검색", 30, 3, 3));
        mockSubjects.add(new Subject("검색 과목2", 30, 3, 3));
        mockSubjects.add(new Subject("과목3", 30, 3, 3));

        for (Subject mockSubject: mockSubjects) {
            mockSubjectResponses.add(new SubjectResponseDto(mockSubject, mockSubject.getClassTime()));
        }
    }

//    @Test
//    @WithMockUser
//    @DisplayName("/ 엔드포인트로 진입하면 과목 DTO 리스트가 모델에 할당돼서 뷰로 전달진다.")
//    void getAllSubjects() throws Exception {
//        // when, then
//        // mock 서비스 메소드 반환타입 지정
//        when(subjectService.findAll()).thenReturn(mockSubjectResponses);
//
//        // GET 요청 및 뷰 및 모델 검증
//        mockMvc.perform(get("/")) // GET 요청 수행
//                .andExpect(status().isOk()) // 상태 검증
//                .andExpect(view().name("main"))
//                .andExpect(model().attributeExists("subjects"))
//                .andExpect(model().attribute("subjects", mockSubjectResponses));
//    }

//    @Test
//    @WithMockUser
//    @DisplayName("/api/search 엔드포인트로 진입하면 쿼리 매개값 기반 과목 DTO 리스트가 모델에 할당돼서 뷰로 전달진다.")
//    void getSubjectByInput() throws Exception {
//        // when, then
//        // mock 서비스 메소드 반환타입 지정
//        when(subjectService.findByKeyword(anyString())).thenReturn(mockSubjectResponses);
//
//        // GET 요청 및 뷰 및 모델 검증
//        ResultActions resultActions =
//                mockMvc.perform(get("/api/search").param("input", "검색"))
//                .andExpect(status().isOk()) // 상태 검증
//                .andExpect(view().name("main"))
//                .andExpect(model().attributeExists("subjects"))
//                .andExpect(model().attribute("subjects", mockSubjectResponses));
//    }
}