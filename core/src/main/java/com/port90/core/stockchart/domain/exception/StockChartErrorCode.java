package com.port90.core.stockchart.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StockChartErrorCode {

    STOCK_CHART_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 차트 타입이 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
