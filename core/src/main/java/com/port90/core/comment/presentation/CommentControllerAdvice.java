package com.port90.core.comment.presentation;

import com.port90.core.comment.domain.error.CommentException;
import com.port90.core.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommentControllerAdvice {

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<?> handleCommentException(CommentException e) {
        log.info("Error Message: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(
                        ErrorResponse.of(e.getErrorCode().name(), e.getMessage())
                );
    }
}
