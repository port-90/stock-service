package com.port90.core.comment.presentation.request;

import com.port90.core.comment.domain.model.CommentDelete;
import jakarta.annotation.Nullable;

public record CommentDeleteRequest(
        @Nullable String password
) {
    public CommentDelete toCommand(Long userId, Long commentId) {
        return CommentDelete.builder()
                .commentId(commentId)
                .userId(userId)
                .password(password)
                .build();
    }
}
