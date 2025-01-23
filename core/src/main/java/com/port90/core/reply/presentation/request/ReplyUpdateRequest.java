package com.port90.core.reply.presentation.request;

import com.port90.core.reply.domain.model.ReplyUpdate;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReplyUpdateRequest(
        @NotBlank @Size(max = 300) String content,
        @Nullable String password
) {
    public ReplyUpdate toCommand(Long userId, Long replyId) {
        return ReplyUpdate.builder()
                .replyId(replyId)
                .userId(userId)
                .content(content)
                .password(password)
                .build();
    }
}
