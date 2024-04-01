package com.quokka.classmate.domain.dto;

import com.quokka.classmate.domain.entity.RegisteredSubject;
import lombok.Getter;

@Getter
public class CartResponseDto {
    private String title;
    private Integer limitCount;
    private Integer time;
    private Integer credit;
    private boolean isRegistered;

    public CartResponseDto(RegisteredSubject registeredSubject) {
        this.title = registeredSubject.getSubject().getTitle();
        this.limitCount = registeredSubject.getSubject().getLimitCount();
        this.time = registeredSubject.getSubject().getTime();
        this.credit = registeredSubject.getSubject().getCredit();
        this.isRegistered = registeredSubject.isRegistered();
    }
}
