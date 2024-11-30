package com.port90.core.auth.exception;

import com.port90.core.auth.domain.exception.UserException;
import com.port90.core.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.port90.core.auth")
public class UserExceptionHandler {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleUserException(UserException e) {
        log.info("Error Code: {}", e.getErrorCode().name());
        log.info("Error Message: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.from(e.getErrorCode().name(), e.getMessage()));
    }
}
