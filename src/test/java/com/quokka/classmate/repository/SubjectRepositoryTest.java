//package com.quokka.classmate.repository;
//
//import com.quokka.classmate.domain.entity.Subject;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest // 테스트 종료 후, 트랜잭션 롤백이 돼서 실제 DB에 저장되지 않음
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//// 테스트용 데이터베이스를 사용하는 것이 아닌, 실제 연동된 데이터베이스를 기반으로 테스트하겠다는 의미
//// 얘가 추후에 어떤 영향을 끼칠 지는 추가 확인 더 필요
//class SubjectRepositoryTest {
//
//    @Autowired
//    private SubjectRepository subjectRepository;
//
//    @Test
//    @DisplayName("특정 키워드를 기반으로 해당되는 명칭의 모든 과목들이 조회된다.")
//    void findByNameContaining() {
//        // given
//        Subject[] mockSubjects = {
//                new Subject("과목1 검색", 30, 3, 3),
//                new Subject("검색 과목2", 30, 3, 3),
//                new Subject("과목3", 30, 3, 3)
//        };
//
//        subjectRepository.saveAll(Arrays.asList(mockSubjects));
//
//        // when
//        List<Subject> findSubjects = subjectRepository.findByTitleContaining("검색");
//
//        // then
//        assertThat(findSubjects)
//                .isNotNull() // 검색 결과가 null 이 아님
//                .hasSize(2) // 두 개의 과목이 검색되어야 함
//                .extracting(Subject::getTitle) // 과목 목록에서 과목 이름만 추출
//                .containsExactly("과목1 검색", "검색 과목2"); // 정확한 과목 이름을 포함해야 함
//    }
//}