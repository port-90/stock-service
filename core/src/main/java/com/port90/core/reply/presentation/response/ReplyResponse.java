package com.port90.core.reply.presentation.response;

import com.port90.core.reply.domain.model.ReplyDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReplyResponse(
        Long id,
        String writer,
        String content,
        Long likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static List<ReplyResponse> from(List<ReplyDto> replies) {
        return replies
                .stream()
                .map(ReplyResponse::from)
                .toList();
    }

    private static ReplyResponse from(ReplyDto reply) {
        return ReplyResponse.builder()
                .id(reply.id())
                .writer(reply.writer())
                .content(reply.content())
                .likeCount(reply.likeCount())
                .createdAt(reply.createdAt())
                .updatedAt(reply.updatedAt())
                .build();
    }
}
