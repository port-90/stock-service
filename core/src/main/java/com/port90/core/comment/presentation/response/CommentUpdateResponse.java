package com.port90.core.comment.presentation.response;

import com.port90.core.comment.domain.model.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentUpdateResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {
    public static CommentUpdateResponse from(Comment comment) {
        return CommentUpdateResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .version(comment.getVersion())
                .build();
    }
}
