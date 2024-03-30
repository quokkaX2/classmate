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
}