package com.port90.core.comment.presentation.request;

import com.port90.core.comment.domain.model.CommentUpdate;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(
        @NotBlank @Size(max = 300) String content,
        @Nullable String password
) {
    public CommentUpdate toCommand(Long userId, Long commentId) {
        return CommentUpdate.builder()
                .commentId(commentId)
                .userId(userId)
                .content(content)
                .password(password)
                .build();
    }
}
