package com.port90.core.comment.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode {

    PARENT_COMMENT_IS_CHILD_COMMENT(BAD_REQUEST, "부모 댓글이 자식 댓글입니다."),
    GUEST_PASSWORD_REQUIRED(BAD_REQUEST, "비회원 비밀번호가 필요합니다"),
    COMMENT_NOT_FOUND(BAD_REQUEST, "댓글이 존재하지 않습니다."),
    COMMENT_USER_UNMATCHED(BAD_REQUEST, "댓글을 작성한 회원이 아닙니다."),
    COMMENT_PASSWORD_UNMATCHED(BAD_REQUEST, "댓글 비밀번호가 일치하지 않습니다."),
    AUTHENTICATION_REQUIRED(BAD_REQUEST, "접근 권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
