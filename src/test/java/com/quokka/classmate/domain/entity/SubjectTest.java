package com.quokka.classmate.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SubjectTest {

    @Test
    @DisplayName("수강 제한인원이 0이 되기 전까지는 수강신청이 가능.")
    void cutCount() {
        // given
        Subject mockSubject = new Subject("가짜과목", 3, 3, 3);
        Integer limitCount = mockSubject.getLimitCount();

        // when
        for (int i = 0; i < limitCount; i++) {
            mockSubject.cutCount();
        }

        // then
        assertThat(mockSubject.getLimitCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("수강 제한인원이 다 찼을 때, 수강 신청을 시도하면 IllegalArgumentException 발생.")
    void cutCountException() {
        // given
        Subject mockEndSubject = new Subject("가짜과목", 0, 3, 3);

        // when
        // then
        Throwable exception =
                assertThrows(IllegalArgumentException.class, mockEndSubject::cutCount);

        assertThat("수강 인원이 다 찼습니다").isEqualTo(exception.getMessage());
    }
}