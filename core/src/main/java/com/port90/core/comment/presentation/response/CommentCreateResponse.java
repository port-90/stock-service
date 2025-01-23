package com.port90.core.comment.presentation.response;

import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.domain.model.CommentType;
import com.port90.core.stockchart.domain.StockChartMinuteId;

import java.time.LocalDateTime;

public record CommentCreateResponse(
        Long id,
        StockChartMinuteId stockChartMinuteId,
        Long userId,
        CommentType type,
        String password,
        String content,
        Long likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {
    public static CommentCreateResponse from(Comment comment) {

        return new CommentCreateResponse(
                comment.getId(),
                comment.getStockChartMinuteId(),
                comment.getUserId(),
                comment.getType(),
                comment.getPassword(),
                comment.getContent(),
                comment.getLikeCount(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getVersion()
        );
    }
}
