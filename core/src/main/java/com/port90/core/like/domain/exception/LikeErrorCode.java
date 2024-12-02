package com.port90.core.like.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode {

    LIKE_ALREADY_EXISTS(BAD_REQUEST, "좋아요를 누른 이력이 존재합니다."),
    LIKE_NOT_FOUND(BAD_REQUEST, "좋아요가 존재하지 않습니다."),
    LIKE_USER_UNMATCHED(BAD_REQUEST, "좋아요를 누른 사용자가 아닙니다."),
    LIKE_CAN_NOT_BE_LESS_THAN_ZERO(BAD_REQUEST, "좋아요 수는 0보다 작아질 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
