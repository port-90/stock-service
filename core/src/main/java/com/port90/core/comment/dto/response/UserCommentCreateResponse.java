package com.port90.core.comment.dto.response;

import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.domain.model.UserComment;

import java.time.LocalDateTime;

public record UserCommentCreateResponse(
        Long commentId,
        String stockCode,
        String content,
        Long parentId,
        int likeCount,
        Long userId,
        String author,
        LocalDateTime createdAt
) {
    public static UserCommentCreateResponse from(Comment comment) {
        UserComment userComment = (UserComment) comment;

        return new UserCommentCreateResponse(
                userComment.getId(),
                userComment.getStockCode(),
                userComment.getContent(),
                userComment.getParentId(),
                userComment.getLikeCount(),
                userComment.getUserId(),
                userComment.getAuthor(),
                userComment.getCreatedAt()
        );
    }
}
