package com.quokka.classmate.domain.dto;

import com.quokka.classmate.domain.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubjectResponseDto {
    private Long id;
    private String title;
    private Integer limitCount;
    private String time;
    private Integer credit;

    public SubjectResponseDto(Subject subject, String time) {
        this.id = subject.getId();
        this.title = subject.getTitle();
        this.limitCount = subject.getLimitCount();
        this.time = time;
        this.credit = subject.getCredit();
    }
}
