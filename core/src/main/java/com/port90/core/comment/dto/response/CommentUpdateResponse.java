package com.port90.core.comment.dto.response;

import com.port90.core.comment.domain.model.Comment;

import java.time.LocalDateTime;

public record CommentUpdateResponse(
        Long commentId,
        String content,
        LocalDateTime updatedAt
) {
    public static CommentUpdateResponse from(Comment comment) {
        return new CommentUpdateResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUpdatedAt()
        );
    }
}
