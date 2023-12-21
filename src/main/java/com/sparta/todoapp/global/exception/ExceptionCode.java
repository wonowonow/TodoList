package com.sparta.todoapp.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저는 존재하지 않습니다"),
    NOT_FOUND_TODO(HttpStatus.NOT_FOUND, "해당 투 두 카드는 존재하지 않습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당 댓글은 존재하지 않습니다"),

    FORBIDDEN_EDIT_ONLY_WRITER(HttpStatus.FORBIDDEN, "작성자만 수정 할 수 있습니다"),
    FORBIDDEN_DELETE_ONLY_WRITER(HttpStatus.FORBIDDEN, "작성자만 삭제 할 수 있습니다"),

    BAD_REQUEST_USERNAME_ALREADY_IN_USE(HttpStatus.BAD_REQUEST, "중복된 username입니다"),
    BAD_REQUEST_UPROAD_FILE(HttpStatus.BAD_REQUEST, "파일 업로드 중 문제가 발생했습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
