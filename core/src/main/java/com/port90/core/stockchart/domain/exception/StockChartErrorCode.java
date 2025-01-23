package com.port90.core.stockchart.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StockChartErrorCode {

    STOCK_CHART_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 차트 타입이 존재하지 않습니다."),
    STOCK_CHART_MINUTE_NOT_FOUND_BY_STOCK_CODE(HttpStatus.BAD_REQUEST, "Stock Code %s에 해당하는 Stock Chart Minute 데이터가 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
