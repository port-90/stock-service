package com.port90.core.like.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.port90.core.like.domain.model.LikeTarget.*;

@Getter
@Builder
public class Like {
    private Long id;
    private Long userId;
    private LikeTarget target;
    private Long targetId;
    private LocalDateTime createdAt;

    public static Like create(LikeCreate likeCreate) {
        return Like.builder()
                .userId(likeCreate.userId())
                .target(likeCreate.target())
                .targetId(likeCreate.targetId())
                .build();
    }

    public boolean isCommentLike() {
        return this.target == COMMENT;
    }

    public boolean isReplyLike() {
        return this.target == REPLY;
    }

    public boolean validateUserId(LikeDelete likeDelete) {
        return !Objects.equals(this.userId, likeDelete.userId());
    }
}
