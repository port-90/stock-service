package com.port90.comment.common.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String code,
        String message,
        LocalDateTime timestamp
) {
    public static ErrorResponse from(String code, String message) {
        return new ErrorResponse(code, message, LocalDateTime.now());
    }

}
