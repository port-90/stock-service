package com.port90.core.reply.domain.error;

import lombok.Getter;

@Getter
public class ReplyException extends RuntimeException {

    private final ReplyErrorCode errorCode;

    public ReplyException(ReplyErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ReplyException(ReplyErrorCode errorCode, Long replyId) {
        super(String.format(errorCode.getMessage(), replyId));
        this.errorCode = errorCode;
    }
}
