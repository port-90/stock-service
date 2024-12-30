package com.port90.core.comment.infrastructure.impl.repository.persistence.mapper;

import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;

public class CommentMapper {

    public static CommentEntity toEntity(Comment comment) {
        return CommentEntity.builder()
                .id(comment.getId())
                .stockCode(comment.getStockCode())
                .date(comment.getDate())
                .time(comment.getTime())
                .userId(comment.getUserId())
                .type(comment.getType())
                .password(comment.getPassword())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .likeCount(comment.getLikeCount())
                .isParent(comment.isParent())
                .isChild(comment.isChild())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .version(comment.getVersion())
                .build();
    }

    public static Comment toModel(CommentEntity commentEntity) {
        return Comment.builder()
                .id(commentEntity.getId())
                .stockCode(commentEntity.getStockCode())
                .date(commentEntity.getDate())
                .time(commentEntity.getTime())
                .userId(commentEntity.getUserId())
                .type(commentEntity.getType())
                .password(commentEntity.getPassword())
                .content(commentEntity.getContent())
                .parentId(commentEntity.getParentId())
                .likeCount(commentEntity.getLikeCount())
                .isParent(commentEntity.isParent())
                .isChild(commentEntity.isChild())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .version(commentEntity.getVersion())
                .build();
    }
}
