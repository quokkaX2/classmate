package com.quokka.classmate.domain.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ResponseDto<T> {
    private final boolean success;
    private final HttpStatus status;
    private final String message;
    private final T data;

}
