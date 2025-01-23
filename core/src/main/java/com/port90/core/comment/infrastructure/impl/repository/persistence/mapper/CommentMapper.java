package com.port90.core.comment.infrastructure.impl.repository.persistence.mapper;

import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;
import com.port90.core.stockchart.domain.StockChartMinuteId;

public class CommentMapper {

    public static CommentEntity toEntity(Comment comment) {
        return CommentEntity.builder()
                .id(comment.getId())
                .stockCode(comment.getStockChartMinuteId().stockCode())
                .date(comment.getStockChartMinuteId().date())
                .time(comment.getStockChartMinuteId().time())
                .userId(comment.getUserId())
                .type(comment.getType())
                .password(comment.getPassword())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .version(comment.getVersion())
                .build();
    }

    public static Comment toModel(CommentEntity commentEntity) {
        return Comment.builder()
                .id(commentEntity.getId())
                .stockChartMinuteId(
                        StockChartMinuteId.of(
                                commentEntity.getStockCode(), commentEntity.getDate(), commentEntity.getTime()
                        )
                )
                .userId(commentEntity.getUserId())
                .type(commentEntity.getType())
                .password(commentEntity.getPassword())
                .content(commentEntity.getContent())
                .likeCount(commentEntity.getLikeCount())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .version(commentEntity.getVersion())
                .build();
    }
}
