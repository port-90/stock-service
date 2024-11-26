package com.port90.core.comment.domain.exception;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException {
    private final ErrorCode errorCode;

    public CommentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
