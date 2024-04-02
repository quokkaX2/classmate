package com.quokka.classmate.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // 테스트 종료 후, 트랜잭션 롤백이 돼서 실제 DB에 저장되지 않음
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// 테스트용 데이터베이스를 사용하는 것이 아닌, 실제 연동된 데이터베이스를 기반으로 테스트하겠다는 의미
// 얘가 추후에 어떤 영향을 끼칠 지는 추가 확인 더 필요
class RegisteredSubjectRepositoryTest {

    @Autowired
    private RegisteredSubjectRepository registeredSubjectRepository;


    @Test
    @DisplayName("학생 엔티티와 과목 엔티티를 기반으로 장바구니 등록 과목을 조회할 수 있다.")
    // 엔티티 기반과 아이디 기반 서로 비교 필요
    void findByStudentAndSubject() {
        // given

        // when

        // then
    }
}