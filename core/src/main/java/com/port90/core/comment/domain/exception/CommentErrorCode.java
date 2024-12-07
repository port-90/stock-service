package com.port90.core.comment.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode {

    INVALID_COMMENT(BAD_REQUEST, "유효하지 않은 댓글입니다."),
    PARENT_COMMENT_IS_CHILD_COMMENT(BAD_REQUEST, "부모 댓글이 자식 댓글입니다."),
    GUEST_PASSWORD_REQUIRED(BAD_REQUEST, "비회원 비밀번호가 필요합니다"),
    COMMENT_NOT_FOUND(BAD_REQUEST, "댓글이 존재하지 않습니다."),
    COMMENT_USER_UNMATCHED(BAD_REQUEST, "회원 정보가 일치하지 않습니다."),
    GUEST_PASSWORD_UNMATCHED(BAD_REQUEST, "비회원 비밀번호가 일치하지 않습니다."),

    STOCK_CODE_NOT_FOUND(BAD_REQUEST, "주식 코드가 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
