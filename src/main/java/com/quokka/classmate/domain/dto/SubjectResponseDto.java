package com.quokka.classmate.domain.dto;

import com.quokka.classmate.domain.entity.Subject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectResponseDto {
    private String title;
    private Integer limitCount;
    private Integer time;
    private Integer credit;

    public SubjectResponseDto(Subject subject) {
        this.title = subject.getTitle();
        this.limitCount = subject.getLimitCount();
        this.time = subject.getTime();
        this.credit = subject.getCredit();
    }
}
