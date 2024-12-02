package com.port90.aggregator.exception;

import lombok.Getter;

@Getter
public class StockDataEmptyException extends RuntimeException {

    private final StockErrorCode stockErrorCode;

    public StockDataEmptyException(StockErrorCode code) {
        super(code.getMessage());
        this.stockErrorCode = code;
    }
}
