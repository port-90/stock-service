package com.port90.core.auth.domain.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final ErrorCode errorCode;

    public UserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UserException(ErrorCode errorCode, Long userId) {
        super(String.format(errorCode.getMessage(), userId));
        this.errorCode = errorCode;
    }
}
