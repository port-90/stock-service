package com.port90.core.comment.dto;

import com.port90.core.comment.domain.model.Comment;

import java.time.LocalDateTime;

public record CommentDto(
        Long commentId,
        String stockCode,
        String author,
        String content,
        int likeCount,
        boolean isParent,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentDto fromGuestOrAnonymousUser(Comment comment, String guestName) {
        return new CommentDto(
                comment.getId(),
                comment.getStockCode(),
                guestName,
                comment.getContent(),
                comment.getLikeCount(),
                comment.isParent(),
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
                comment.isParent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
