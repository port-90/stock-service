package com.port90.core.comment.domain.model;

import lombok.Builder;

@Builder
public record CommentCreate(
        Long userId,
        String stockCode,
        String content,
        String password,
        Boolean isAnonymous
) {
    public boolean passwordIsNull() {
        return this.password == null;
    }
}
