package com.port90.core.comment.domain.model;

import lombok.Builder;

@Builder
public record CommentUpdate(
        Long commentId,
        Long userId,
        String content,
        String password
) {
    public boolean userIdIsNull() {
        return this.userId == null;
    }
}
