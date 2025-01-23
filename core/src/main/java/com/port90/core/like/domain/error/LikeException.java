package com.port90.core.like.domain.error;

import lombok.Getter;

@Getter
public class LikeException extends RuntimeException {

    private final LikeErrorCode errorCode;

    public LikeException(LikeErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public LikeException(LikeErrorCode errorCode, Long likeId) {
        super(String.format(errorCode.getMessage(), likeId));
        this.errorCode = errorCode;
    }
}
