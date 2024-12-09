package com.port90.core.comment.dto;

import com.port90.core.comment.domain.model.GuestComment;
import com.port90.core.comment.domain.model.UserComment;

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
    public static CommentDto from(UserComment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getStockCode(),
                comment.getAuthor(),
                comment.getContent(),
                comment.getLikeCount(),
                comment.isParent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    public static CommentDto from(UserComment comment, String author) {
        return new CommentDto(
                comment.getId(),
                comment.getStockCode(),
                author,
                comment.getContent(),
                comment.getLikeCount(),
                comment.isParent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    public static CommentDto from(GuestComment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getStockCode(),
                comment.getAuthor(),
                comment.getContent(),
                comment.getLikeCount(),
                comment.isParent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
