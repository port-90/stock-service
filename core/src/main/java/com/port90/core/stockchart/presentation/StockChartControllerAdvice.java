package com.port90.core.stockchart.presentation;

import com.port90.core.common.exception.ErrorResponse;
import com.port90.core.stockchart.domain.exception.StockChartNotFoundException;
import com.port90.core.stockchart.domain.exception.StockChartTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class StockChartControllerAdvice {

    @ExceptionHandler(StockChartNotFoundException.class)
    public ResponseEntity<?> handleStockChartNotFoundException(StockChartNotFoundException e) {
        log.info("Error Message: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(
                        ErrorResponse.of(e.getErrorCode().name(), e.getMessage())
                );
    }

    @ExceptionHandler(StockChartTypeException.class)
    public ResponseEntity<?> handleStockChartTypeException(StockChartTypeException e) {
        log.info("Error Message: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(
                        ErrorResponse.of(e.getErrorCode().name(), e.getMessage())
                );
    }
}
