package com.port90.core.reply.domain.model;

import lombok.Builder;

@Builder
public record ReplyCreate(
        Long userId,
        Long commentId,
        String content,
        String password,
        Boolean isAnonymous
) {
    public boolean passwordIsNull() {
        return this.password == null;
    }
}
