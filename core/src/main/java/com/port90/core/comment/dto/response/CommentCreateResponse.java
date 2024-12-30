package com.port90.core.comment.dto.response;

import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.domain.model.CommentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record CommentCreateResponse(
        Long commentId,
        String stockCode,
        LocalDate date,
        LocalTime time,
        Long userId,
        String password,
        CommentType type,
        String content,
        Long parentId,
        int likeCount,
        LocalDateTime createdAt
) {
    public static CommentCreateResponse from(Comment comment) {

        return new CommentCreateResponse(
                comment.getId(),
                comment.getStockCode(),
                comment.getDate(),
                comment.getTime(),
                comment.getUserId(),
                comment.getPassword(),
                comment.getType(),
                comment.getContent(),
                comment.getParentId(),
                comment.getLikeCount(),
                comment.getCreatedAt()
        );
    }
}
