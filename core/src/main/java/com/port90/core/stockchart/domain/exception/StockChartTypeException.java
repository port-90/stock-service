package com.port90.core.stockchart.domain.exception;

import lombok.Getter;

@Getter
public class StockChartTypeException extends RuntimeException {

    private final StockChartErrorCode errorCode;

    public StockChartTypeException(StockChartErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
