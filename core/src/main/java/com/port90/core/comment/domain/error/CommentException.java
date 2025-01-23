package com.port90.core.comment.domain.error;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException {

    private final CommentErrorCode errorCode;

    public CommentException(CommentErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CommentException(CommentErrorCode errorCode, Long commentId) {
        super(String.format(errorCode.getMessage(), commentId));
        this.errorCode = errorCode;
    }
}
