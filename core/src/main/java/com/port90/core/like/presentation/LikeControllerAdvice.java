package com.port90.core.like.presentation;

import com.port90.core.common.exception.ErrorResponse;
import com.port90.core.like.domain.error.LikeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class LikeControllerAdvice {

    @ExceptionHandler(LikeException.class)
    public ResponseEntity<?> handleLikeException(LikeException e) {
        log.info("Error Message: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(
                        ErrorResponse.of(e.getErrorCode().name(), e.getMessage())
                );
    }
}
