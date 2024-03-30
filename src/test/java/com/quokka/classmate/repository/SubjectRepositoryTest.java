package com.quokka.classmate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SubjectRepositoryTest {

    @Test
    void findByNameContaining() {
    }
}

// 실제 DB와 통신되지 않으면 테스트가 의미가 없으므로 mocking 을 별도로 진행하지 않음
// DB와 연동될 시, 테스트 코드 작성