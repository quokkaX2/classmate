package com.quokka.classmate.domain.dto;

import lombok.Getter;

@Getter
public class RedisQueueRequestDto {
    private Long studentId;
    private Long subjectId;

    public RedisQueueRequestDto(Long studentId, Long subjectId) {
        this.studentId = studentId;
        this.subjectId = subjectId;
    }
}
