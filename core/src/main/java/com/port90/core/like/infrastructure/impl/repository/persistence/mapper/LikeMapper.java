package com.port90.core.like.infrastructure.impl.repository.persistence.mapper;

import com.port90.core.like.domain.model.Like;
import com.port90.core.like.infrastructure.impl.repository.persistence.entity.LikeEntity;

public class LikeMapper {

    public static Like toModel(LikeEntity likeEntity) {
        return Like.builder()
                .id(likeEntity.getId())
                .userId(likeEntity.getUserId())
                .commentId(likeEntity.getCommentId())
                .createdAt(likeEntity.getCreatedAt())
                .updatedAt(likeEntity.getUpdatedAt())
                .build();
    }

    public static LikeEntity toEntity(Like like) {
        return LikeEntity.builder()
                .id(like.getId())
                .userId(like.getUserId())
                .commentId(like.getCommentId())
                .createdAt(like.getCreatedAt())
                .updatedAt(like.getUpdatedAt())
                .build();
    }
}
