package com.port90.core.stockchart.exception;

import com.port90.core.common.exception.ErrorResponse;
import com.port90.core.stockchart.domain.exception.StockChartTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommentExceptionHandler {

    @ExceptionHandler(StockChartTypeException.class)
    public ResponseEntity<ErrorResponse> handleCommentException(StockChartTypeException e) {
        log.info("Error Code: {}", e.getErrorCode().name());
        log.info("Error Message: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.from(e.getErrorCode().name(), e.getMessage()));
    }
}
