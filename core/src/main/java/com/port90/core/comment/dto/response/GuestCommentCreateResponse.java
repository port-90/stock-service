package com.port90.core.comment.dto.response;

import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.domain.model.GuestComment;

import java.time.LocalDateTime;

public record GuestCommentCreateResponse(
        Long commentId,
        String stockCode,
        String content,
        Long parentId,
        int likeCount,
        String author,
        String password,
        LocalDateTime createdAt
) {
    public static GuestCommentCreateResponse from(Comment comment) {
        GuestComment guestComment = (GuestComment) comment;

        return new GuestCommentCreateResponse(
                guestComment.getId(),
                guestComment.getStockCode(),
                guestComment.getContent(),
                guestComment.getParentId(),
                guestComment.getLikeCount(),
                guestComment.getAuthor(),
                guestComment.getPassword(),
                guestComment.getCreatedAt()
        );
    }
}
