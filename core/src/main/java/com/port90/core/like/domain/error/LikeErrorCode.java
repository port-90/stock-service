package com.port90.core.like.domain.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode {

    LIKE_ALREADY_EXISTS(BAD_REQUEST, "좋아요를 누른 이력이 존재합니다."),
    LIKE_NOT_FOUND_BY_ID(BAD_REQUEST, "Like Id %d에 해당하는 Like 데이터가 존재하지 않습니다."),
    LIKE_USER_MISMATCH(BAD_REQUEST, "좋아요를 누른 사용자가 아닙니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
