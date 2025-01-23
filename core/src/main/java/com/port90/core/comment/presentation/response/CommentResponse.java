package com.port90.core.comment.presentation.response;

import com.port90.core.comment.domain.model.CommentDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentResponse(
        Long id,
        String writer,
        String content,
        Long replyCount,
        Long likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static List<CommentResponse> from(List<CommentDto> comments) {
        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    private static CommentResponse from(CommentDto comment) {
        return CommentResponse.builder()
                .id(comment.id())
                .writer(comment.writer())
                .content(comment.content())
                .replyCount(comment.replyCount())
                .likeCount(comment.likeCount())
                .createdAt(comment.createdAt())
                .updatedAt(comment.updatedAt())
                .build();
    }
}
