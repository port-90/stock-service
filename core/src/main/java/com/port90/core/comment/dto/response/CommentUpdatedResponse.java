package com.port90.core.comment.dto.response;

import com.port90.core.comment.domain.model.Comment;

import java.time.LocalDateTime;

public record CommentUpdatedResponse(
        Long commentId,
        Long userId,
        String guestId,
        String content,
        LocalDateTime updatedAt
) {
    public static CommentUpdatedResponse from(Comment comment) {
        return new CommentUpdatedResponse(
                comment.getId(),
                comment.getUserId(),
                comment.getGuestId(),
                comment.getContent(),
                comment.getUpdatedAt()
        );
    }
}
