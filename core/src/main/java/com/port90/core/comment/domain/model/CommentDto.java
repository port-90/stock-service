package com.port90.core.comment.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentDto(
        Long id,
        String writer,
        String content,
        Long replyCount,
        Long likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentDto from(Comment comment, String writer, Long replyCount) {
        return CommentDto.builder()
                .id(comment.getId())
                .writer(writer)
                .content(comment.getContent())
                .replyCount(replyCount)
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
