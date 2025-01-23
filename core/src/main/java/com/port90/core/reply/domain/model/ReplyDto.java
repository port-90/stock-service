package com.port90.core.reply.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReplyDto(
        Long id,
        String writer,
        String content,
        Long likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReplyDto from(Reply reply, String writer) {
        return ReplyDto.builder()
                .id(reply.getId())
                .writer(writer)
                .content(reply.getContent())
                .likeCount(reply.getLikeCount())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();
    }
}
