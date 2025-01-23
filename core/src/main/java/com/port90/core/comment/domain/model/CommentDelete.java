package com.port90.core.comment.domain.model;

import lombok.Builder;

@Builder
public record CommentDelete(
        Long commentId,
        Long userId,
        String password
) {
    public boolean userIdIsNull() {
        return this.userId == null;
    }
}
