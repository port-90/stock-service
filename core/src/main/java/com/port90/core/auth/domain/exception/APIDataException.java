package com.port90.core.auth.domain.exception;

import lombok.Getter;

@Getter
public class APIDataException extends RuntimeException {
    private final ErrorCode errorCode;

    public APIDataException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
