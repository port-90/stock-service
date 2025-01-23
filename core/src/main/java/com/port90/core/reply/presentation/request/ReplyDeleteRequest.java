package com.port90.core.reply.presentation.request;

import com.port90.core.reply.domain.model.ReplyDelete;
import jakarta.annotation.Nullable;

public record ReplyDeleteRequest(
        @Nullable String password
) {
    public ReplyDelete toCommand(Long userId, Long replyId) {
        return ReplyDelete.builder()
                .replyId(replyId)
                .userId(userId)
                .password(password)
                .build();
    }
}
