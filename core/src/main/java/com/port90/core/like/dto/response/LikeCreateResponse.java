package com.port90.core.like.dto.response;

import com.port90.core.like.domain.model.Like;

import java.time.LocalDateTime;

public record LikeCreateResponse(
        Long likeId,
        Long userId,
        Long commentId,
        LocalDateTime createdAt
) {
    public static LikeCreateResponse from(Like like) {
        return new LikeCreateResponse(
                like.getId(),
                like.getUserId(),
                like.getCommentId(),
                like.getCreatedAt()
        );
    }
}
