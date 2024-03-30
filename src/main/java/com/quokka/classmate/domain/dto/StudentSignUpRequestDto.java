package com.quokka.classmate.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSignUpRequestDto {
    private String email;
    private String password;
    private String name;
}
