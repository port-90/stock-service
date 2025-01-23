package com.port90.core.comment.domain.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode {
    COMMENT_PASSWORD_REQUIRED(BAD_REQUEST, "댓글 비밀번호가 필요합니다."),
    COMMENT_PASSWORD_MISMATCH(BAD_REQUEST, "댓글 비밀번호가 일치하지 않습니다."),
    COMMENT_USER_MISMATCH(BAD_REQUEST, "댓글 작성자 Id가 일치하지 않습니다."),
    COMMENT_NOT_FOUND_BY_ID(BAD_REQUEST, "Id %d에 해당하는 Comment 데이터가 존재하지 않습니다."),
    COMMENT_NOT_FOUND_BY_USER_ID(BAD_REQUEST, "User Id %d에 해당하는 Comment 데이터가 존재하지 않습니다."),
    COMMENT_WRITER_AUTHENTICATION_REQUIRED(BAD_REQUEST, "작성자 인증이 필요합니다."),
    COMMENT_LIKE_CAN_NOT_BE_LESS_THAN_ZERO(BAD_REQUEST, "댓글 좋아요 수는 0보다 작아질 수 없습니다."),

    UNSUPPORTED_COMMENT_TYPE(INTERNAL_SERVER_ERROR, "지원하지 않는 댓글 타입입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
