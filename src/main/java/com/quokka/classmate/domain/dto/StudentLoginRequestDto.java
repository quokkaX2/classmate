package com.quokka.classmate.domain.dto;

import lombok.Getter;

@Getter
public class StudentLoginRequestDto {
    private String email;
    private String password;
}
