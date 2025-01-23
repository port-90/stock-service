package com.port90.core.reply.presentation.response;

import com.port90.core.reply.domain.model.Reply;
import com.port90.core.reply.domain.model.ReplyType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReplyCreateResponse(
        Long id,
        Long userId,
        Long commentId,
        ReplyType type,
        String password,
        String content,
        Long likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {
    public static ReplyCreateResponse from(Reply reply) {
        return ReplyCreateResponse.builder()
                .id(reply.getId())
                .userId(reply.getUserId())
                .commentId(reply.getCommentId())
                .type(reply.getType())
                .password(reply.getPassword())
                .content(reply.getContent())
                .likeCount(reply.getLikeCount())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .version(reply.getVersion())
                .build();
    }
}
