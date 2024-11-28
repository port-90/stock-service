package com.port90.core.auth.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_ADDED_FAVORITE_STOCK(HttpStatus.CONFLICT, "이미 추가된 관심 주식입니다."),
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "관심 주식이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
