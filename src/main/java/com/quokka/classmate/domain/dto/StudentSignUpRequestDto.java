package com.quokka.classmate.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class StudentSignUpRequestDto {
    private String email;
    private String password;
    private String name;
}
