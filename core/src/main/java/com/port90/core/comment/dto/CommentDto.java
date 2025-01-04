package com.port90.core.comment.dto;

import com.port90.core.comment.domain.model.Comment;

import java.time.LocalDateTime;

public record CommentDto(
        Long commentId,
        String stockCode,
        String author,
        String content,
        long childCommentCount,
        int likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentDto from(Comment comment, String author, long childCommentCount) {
        return new CommentDto(
                comment.getId(),
                comment.getStockCode(),
                author,
                comment.getContent(),
                childCommentCount,
                comment.getLikeCount(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
