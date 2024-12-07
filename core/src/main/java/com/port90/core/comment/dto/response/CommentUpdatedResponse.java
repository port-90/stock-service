package com.port90.core.comment.dto.response;

import com.port90.core.comment.domain.model.Comment;

import java.time.LocalDateTime;

public record CommentUpdatedResponse(
        Long commentId,
        String stockCode,
        String content,
        LocalDateTime updatedAt
) {
    public static CommentUpdatedResponse from(Comment comment) {
        return new CommentUpdatedResponse(
                comment.getId(),
                comment.getStockCode(),
                comment.getContent(),
                comment.getUpdatedAt()
        );
    }
}
