package com.port90.core.stockchart.domain.exception;

import lombok.Getter;

@Getter
public class StockChartNotFoundException extends RuntimeException {

    private final StockChartErrorCode errorCode;

    public StockChartNotFoundException(StockChartErrorCode errorCode, String stockCode) {
        super(String.format(errorCode.getMessage(), stockCode));
        this.errorCode = errorCode;
    }
}
