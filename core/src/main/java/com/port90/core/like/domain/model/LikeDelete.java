package com.port90.core.like.domain.model;

import lombok.Builder;

@Builder
public record LikeDelete(
        Long userId,
        Long likeId
) {
    public static LikeDelete of(Long userId, Long likeId) {
        return LikeDelete.builder()
                .userId(userId)
                .likeId(likeId)
                .build();
    }
}
