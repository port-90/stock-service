package com.port90.core.reply.domain.model;

import lombok.Builder;

@Builder
public record ReplyDelete(
        Long replyId,
        Long userId,
        String password
) {
    public boolean userIdIsNull() {
        return this.userId == null;
    }
}
