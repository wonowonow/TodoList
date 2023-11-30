package com.sparta.todoapp.global.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonResponseDto {
    private final HttpStatus httpStatus;
    private final String message;

    public CommonResponseDto(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
