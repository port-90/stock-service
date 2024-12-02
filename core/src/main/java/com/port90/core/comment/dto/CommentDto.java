package com.port90.core.comment.dto;

import com.port90.core.comment.domain.model.Comment;

import java.time.LocalDateTime;

public record CommentDto(
        Long commentId,
        String stockCode,
        String username,
        String content,
        int likeCount,
        boolean hasChildren,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentDto fromGuest(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getStockCode(),
                comment.getGuestId(),
                comment.getContent(),
                comment.getLikeCount(),
                comment.isHasChildren(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    public static CommentDto fromUser(Comment comment, String username) {
        return new CommentDto(
                comment.getId(),
                comment.getStockCode(),
                username,
                comment.getContent(),
                comment.getLikeCount(),
                comment.isHasChildren(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
