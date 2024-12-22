package com.port90.core.like.domain.exception;

public class LikeException extends RuntimeException {

    private final LikeErrorCode errorCode;

    public LikeException(LikeErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public LikeException(LikeErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
