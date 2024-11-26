package com.port90.core.comment.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    PARENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "부모 댓글이 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
