package com.port90.core.comment.presentation.request;

import com.port90.core.comment.domain.model.CommentCreate;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
        @NotBlank String stockCode,
        @NotBlank @Size(max = 300) String content,
        @Nullable String password,
        @Nullable Boolean isAnonymous
) {
    public CommentCreate toCommand(Long userId) {
        return CommentCreate.builder()
                .userId(userId)
                .stockCode(stockCode)
                .content(content)
                .password(password)
                .isAnonymous(isAnonymous)
                .build();
    }
}
