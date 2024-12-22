package com.port90.core.comment.infrastructure.impl.repository.persistence.mapper;

import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.domain.model.GuestComment;
import com.port90.core.comment.domain.model.UserComment;
import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;
import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.GuestCommentEntity;
import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.UserCommentEntity;

public class CommentMapper {

    public static CommentEntity toEntity(Comment comment) {
        if (comment instanceof UserComment userComment) {
            return UserCommentEntity.builder()
                    .id(userComment.getId())
                    .stockCode(userComment.getStockCode())
                    .content(userComment.getContent())
                    .author(userComment.getAuthor())
                    .parentId(userComment.getParentId())
                    .likeCount(userComment.getLikeCount())
                    .isParent(userComment.isParent())
                    .isChild(userComment.isChild())
                    .userId(userComment.getUserId())
                    .isAnonymous(userComment.isAnonymous())
                    .createdAt(userComment.getCreatedAt())
                    .updatedAt(userComment.getUpdatedAt())
                    .version(userComment.getVersion())
                    .build();
        }

        GuestComment guestComment = (GuestComment) comment;
        return GuestCommentEntity.builder()
                .id(guestComment.getId())
                .stockCode(guestComment.getStockCode())
                .content(guestComment.getContent())
                .author(guestComment.getAuthor())
                .parentId(guestComment.getParentId())
                .likeCount(guestComment.getLikeCount())
                .isParent(guestComment.isParent())
                .isChild(guestComment.isChild())
                .password(guestComment.getPassword())
                .createdAt(guestComment.getCreatedAt())
                .updatedAt(guestComment.getUpdatedAt())
                .version(guestComment.getVersion())
                .build();
    }

    public static Comment toModel(CommentEntity commentEntity) {
        if (commentEntity instanceof UserCommentEntity userCommentEntity) {
            return UserComment.builder()
                    .id(userCommentEntity.getId())
                    .stockCode(userCommentEntity.getStockCode())
                    .content(userCommentEntity.getContent())
                    .author(userCommentEntity.getAuthor())
                    .parentId(userCommentEntity.getParentId())
                    .likeCount(userCommentEntity.getLikeCount())
                    .isParent(userCommentEntity.isParent())
                    .isChild(userCommentEntity.isChild())
                    .userId(userCommentEntity.getUserId())
                    .isAnonymous(userCommentEntity.isAnonymous())
                    .createdAt(userCommentEntity.getCreatedAt())
                    .updatedAt(userCommentEntity.getUpdatedAt())
                    .version(userCommentEntity.getVersion())
                    .build();
        }

        GuestCommentEntity guestCommentEntity = (GuestCommentEntity) commentEntity;
        return GuestComment.builder()
                .id(guestCommentEntity.getId())
                .stockCode(guestCommentEntity.getStockCode())
                .content(guestCommentEntity.getContent())
                .author(guestCommentEntity.getAuthor())
                .parentId(guestCommentEntity.getParentId())
                .likeCount(guestCommentEntity.getLikeCount())
                .isParent(guestCommentEntity.isParent())
                .isChild(guestCommentEntity.isChild())
                .password(guestCommentEntity.getPassword())
                .createdAt(guestCommentEntity.getCreatedAt())
                .updatedAt(guestCommentEntity.getUpdatedAt())
                .version(guestCommentEntity.getVersion())
                .build();
    }
}
