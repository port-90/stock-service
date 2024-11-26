package com.port90.core.comment.dto.response;

import com.port90.core.comment.domain.model.Comment;

import java.time.LocalDateTime;

public record CommentCreateResponse(
        Long commentId,
        Long userId,
        String guestId,
        String content,
        Long parentId,
        int likeCount,
        LocalDateTime createdAt
) {
    public static CommentCreateResponse from(Comment comment) {
        return new CommentCreateResponse(
                comment.getId(),
                comment.getUserId(),
                comment.getGuestId(),
                comment.getContent(),
                comment.getParentId(),
                comment.getLikeCount(),
                comment.getCreatedAt()
        );
    }
}
