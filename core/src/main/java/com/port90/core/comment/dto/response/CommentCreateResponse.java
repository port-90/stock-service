package com.port90.core.comment.dto.response;

import com.port90.core.comment.domain.model.Comment;

import java.time.LocalDateTime;

public record CommentCreateResponse(
        Long commentId,
        String stockCode,
        Long userId,
        String guestId,
        String guestPassword,
        String content,
        Long parentId,
        int likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentCreateResponse from(Comment comment) {
        return new CommentCreateResponse(
                comment.getId(),
                comment.getStockCode(),
                comment.getUserId(),
                comment.getGuestId(),
                comment.getGuestPassword(),
                comment.getContent(),
                comment.getParentId(),
                comment.getLikeCount(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
