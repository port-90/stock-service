package com.port90.core.comment.infrastructure.impl.repository.persistence;

import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;

public class CommentMapper {

    public static CommentEntity toEntity(Comment comment) {
        return CommentEntity.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .guestId(comment.getGuestId())
                .guestPassword(comment.getGuestPassword())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public static Comment toModel(CommentEntity commentEntity) {
        return Comment.builder()
                .id(commentEntity.getId())
                .userId(commentEntity.getUserId())
                .guestId(commentEntity.getGuestId())
                .guestPassword(commentEntity.getGuestPassword())
                .content(commentEntity.getContent())
                .parentId(commentEntity.getParentId())
                .likeCount(commentEntity.getLikeCount())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .build();
    }
}
