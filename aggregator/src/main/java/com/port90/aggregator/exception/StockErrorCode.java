package com.port90.aggregator.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StockErrorCode {

    STOCK_DATA_EMPTY(HttpStatus.BAD_REQUEST, "주식 데이터가 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
