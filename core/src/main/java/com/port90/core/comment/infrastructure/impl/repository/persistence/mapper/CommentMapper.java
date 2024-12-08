package com.port90.core.comment.infrastructure.impl.repository.persistence.mapper;

import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;

public class CommentMapper {

    public static CommentEntity toEntity(Comment comment) {
        return CommentEntity.builder()
                .id(comment.getId())
                .stockCode(comment.getStockCode())
                .userId(comment.getUserId())
                .guestPassword(comment.getGuestPassword())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .likeCount(comment.getLikeCount())
                .isParent(comment.isParent())
                .isChild(comment.isChild())
                .isUserComment(comment.isUserComment())
                .isAnonymousUserComment(comment.isAnonymousUserComment())
                .isGuestComment(comment.isGuestComment())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public static Comment toModel(CommentEntity commentEntity) {
        return Comment.builder()
                .id(commentEntity.getId())
                .stockCode(commentEntity.getStockCode())
                .userId(commentEntity.getUserId())
                .guestPassword(commentEntity.getGuestPassword())
                .content(commentEntity.getContent())
                .parentId(commentEntity.getParentId())
                .likeCount(commentEntity.getLikeCount())
                .isParent(commentEntity.isParent())
                .isChild(commentEntity.isChild())
                .isUserComment(commentEntity.isUserComment())
                .isAnonymousUserComment(commentEntity.isAnonymousUserComment())
                .isGuestComment(commentEntity.isGuestComment())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .build();
    }
}
