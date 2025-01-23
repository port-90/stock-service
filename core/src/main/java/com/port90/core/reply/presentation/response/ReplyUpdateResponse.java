package com.port90.core.reply.presentation.response;

import com.port90.core.reply.domain.model.Reply;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReplyUpdateResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {
    public static ReplyUpdateResponse from(Reply reply) {
        return ReplyUpdateResponse.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .version(reply.getVersion())
                .build();
    }
}
