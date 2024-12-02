package com.port90.aggregator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class StockAggregateExceptionHandler {

    @ExceptionHandler(StockDataEmptyException.class)
    public ResponseEntity<ErrorResponse> handleStockDataEmptyException(StockDataEmptyException e) {
        log.error("StockDataEmptyException: {}", e.getMessage());
        return ResponseEntity
                .status(e.getStockErrorCode().getStatus())
                .body(ErrorResponse.from(e.getStockErrorCode().name(), e.getMessage()));
    }
}
