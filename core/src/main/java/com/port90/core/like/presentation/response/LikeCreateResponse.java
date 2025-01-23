package com.port90.core.like.presentation.response;

import com.port90.core.like.domain.model.Like;
import com.port90.core.like.domain.model.LikeTarget;

import java.time.LocalDateTime;

public record LikeCreateResponse(
        Long likeId,
        Long userId,
        LikeTarget target,
        Long targetId,
        LocalDateTime createdAt
) {
    public static LikeCreateResponse from(Like like) {
        return new LikeCreateResponse(
                like.getId(),
                like.getUserId(),
                like.getTarget(),
                like.getTargetId(),
                like.getCreatedAt()
        );
    }
}
