package com.sparta.todoapp.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse {

    private final HttpStatus httpStatus;
    private final String message;

    public ExceptionResponse(ExceptionCode exceptionCode) {
        this.httpStatus = exceptionCode.getHttpStatus();
        this.message = exceptionCode.getMessage();
    }
}
