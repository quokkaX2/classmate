package com.quokka.classmate.service;

import com.quokka.classmate.domain.repository.SubjectRepository;
import com.quokka.classmate.domain.service.SubjectService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class) // JUnit 5와 Mockito 연동
class SubjectServiceTest {

    @Mock // Mock 객체 생성
    private SubjectRepository subjectRepository;

    @InjectMocks // Mock 객체 레포지토리 주입
    private SubjectService subjectService;

//    @Test
//    @DisplayName("강의 과목 전체 조회 시, 누락되는 과목이 없어야 한다.")
//    void findAll() {
//        // given
//        // Mock 데이터 담기용 리스트 생성
//        List<Subject> mockSubjects = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Subject subject = new Subject("가짜과목 " + i, 30, 3, 3);
//            mockSubjects.add(subject);
//        }
//        // 스프링 프로젝트에서의 컨테이너에 등록된 빈을 테스트코드에도 주입받을 수 없음
//        // 그래서 임의로 해당 메소드의 결과값을 강제 조정
//        when(subjectRepository.findAll()).thenReturn(mockSubjects);
//
//        // when
//        List<SubjectResponseDto> result = subjectService.findAll();
//
//        // then
//        assertThat(result.size()).isEqualTo(10);
//
//        for (int i = 0; i < 10; i++) {
//            assertThat(result.get(i).getTitle()).isEqualTo("가짜과목 " + i);
//            assertThat(result.get(i).getLimitCount()).isEqualTo(30);
//            assertThat(result.get(i).getTime()).isEqualTo("월요일 11:00 ~ 12:00");
//            assertThat(result.get(i).getCredit()).isEqualTo(3);
//        }
//    }

//    @Test
//    @DisplayName("과목 키워드 검색 시, 해당 키워드를 포함하는 과목이 반환되어야 한다.")
//    void testFindByKeyword() {
//        // given
//        String keyword = "검색";
//        List<Subject> mockSubjects = new ArrayList<>();
//        mockSubjects.add(new Subject("과목1 검색", 30, 3, 3));
//        mockSubjects.add(new Subject("검색 과목2", 30, 3, 3));
//        mockSubjects.add(new Subject("과목3", 30, 3, 3));
//        when(subjectRepository.findByTitleContaining(keyword)).thenReturn(
//                mockSubjects.stream().filter(subject -> subject.getTitle().contains(keyword)).toList());
//
//        // when
//        List<SubjectResponseDto> result = subjectService.findByKeyword(keyword);
//
//        // then
//        assertThat(result.size()).isEqualTo(2); // 검색 키워드 포함하는 단어 개수
//        assertThat(result.get(0).getTitle()).isEqualTo("과목1 검색");
//        assertThat(result.get(1).getTitle()).isEqualTo("검색 과목2");
//    }
}