package com.port90.core.reply.domain.model;

import lombok.Builder;

@Builder
public record ReplyUpdate(
        Long replyId,
        Long userId,
        String content,
        String password
) {
    public boolean userIdIsNull() {
        return this.userId == null;
    }
}
