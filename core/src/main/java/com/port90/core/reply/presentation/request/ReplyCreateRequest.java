package com.port90.core.reply.presentation.request;

import com.port90.core.reply.domain.model.ReplyCreate;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReplyCreateRequest(
        @NotNull Long commentId,
        @NotBlank @Size(max = 300) String content,
        @Nullable String password,
        @Nullable Boolean isAnonymous
) {
    public ReplyCreate toCommand(Long userId) {
        return ReplyCreate.builder()
                .userId(userId)
                .commentId(commentId)
                .content(content)
                .password(password)
                .isAnonymous(isAnonymous)
                .build();
    }
}
