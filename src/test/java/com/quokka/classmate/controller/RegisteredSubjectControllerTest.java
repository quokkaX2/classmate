package com.quokka.classmate.controller;

import com.quokka.classmate.domain.controller.routing.RegisteredSubjectController;
import com.quokka.classmate.domain.service.RegisteredSubjectService;
import com.quokka.classmate.domain.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(RegisteredSubjectController.class)
@MockBean(JpaMetamodelMappingContext.class)
class RegisteredSubjectControllerTest {

    @Autowired
    private MockMvc mockMvc; // HTTP 호출 담당

    @MockBean
    private RegisteredSubjectService registeredSubjectService;

    @MockBean
    private SubjectService subjectService;


//    @Test
//    @DisplayName("장바구니에 강의를 등록해둘 수 있다.")
//    @WithMockUser(roles="USER") // 사용자의 역할을 설정
//    void saveRegisteredSubject() throws Exception {
//        // given
//        Long mockSubjectId = 1L;
//        when(registeredSubjectService.createRegisteredSubject(mockSubjectId, null))
//                .thenReturn(ResponseEntity.ok("장바구니에 담겼습니다."));
//
//        // when, then
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/registered-subjects")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"subjectId\":1}"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
}