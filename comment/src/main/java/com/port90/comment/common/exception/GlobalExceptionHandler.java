package com.port90.comment.common.exception;

import com.port90.comment.domain.exception.CommentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<?> handleCommentException(CommentException e) {
        log.info("Error Code: {}", e.getErrorCode().name());
        log.info("Error Message: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.from(e.getErrorCode().name(), e.getMessage()));
    }
}
