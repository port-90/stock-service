package com.port90.core.like.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class Like {
    private Long id;
    private Long userId;
    private Long commentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Like create(Long userId, Long commentId) {
        return Like.builder()
                .userId(userId)
                .commentId(commentId)
                .build();
    }

    public boolean isNotLikedBy(Long userId) {
        return !Objects.equals(this.userId, userId);
    }
}
