package com.quokka.classmate.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRequestDto {
    private String email;
    private String password;
    private String name;
}
