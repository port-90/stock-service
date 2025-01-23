package com.port90.core.reply.domain.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@RequiredArgsConstructor
public enum ReplyErrorCode {

    REPLY_NOT_FOUND_BY_ID(BAD_REQUEST, "Reply Id %d에 해당하는 Reply 데이터가 존재하지 않습니다."),
    REPLY_PASSWORD_REQUIRED(BAD_REQUEST, "답글 비밀번호가 필요합니다."),
    REPLY_PASSWORD_MISMATCH(BAD_REQUEST, "답글 비밀번호가 일치하지 않습니다."),
    REPLY_USER_MISMATCH(BAD_REQUEST, "답글 작성자 Id가 일치하지 않습니다."),
    REPLY_WRITER_AUTHENTICATION_REQUIRED(BAD_REQUEST, "작성자 인증이 필요합니다."),
    REPLY_LIKE_CAN_NOT_BE_LESS_THAN_ZERO(BAD_REQUEST, "답글 좋아요 수는 0보다 작아질 수 없습니다."),

    UNSUPPORTED_REPLY_TYPE(INTERNAL_SERVER_ERROR, "지원하지 않는 답글 타입입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
