package com.port90.core.like.domain.model;

import lombok.Builder;

@Builder
public record LikeCreate(
        Long userId,
        LikeTarget target,
        Long targetId
) {
    public boolean isCommentLikeCreate() {
        return this.target == LikeTarget.COMMENT;
    }

    public boolean isReplyLikeCreate() {
        return this.target == LikeTarget.REPLY;
    }
}
