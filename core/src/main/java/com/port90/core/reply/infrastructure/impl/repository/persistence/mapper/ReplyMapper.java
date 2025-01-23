package com.port90.core.reply.infrastructure.impl.repository.persistence.mapper;

import com.port90.core.reply.domain.model.Reply;
import com.port90.core.reply.infrastructure.impl.repository.persistence.entity.ReplyEntity;

public class ReplyMapper {

    public static ReplyEntity toEntity(Reply reply) {
        return ReplyEntity.builder()
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

    public static Reply toModel(ReplyEntity replyEntity) {
        return Reply.builder()
                .id(replyEntity.getId())
                .userId(replyEntity.getUserId())
                .commentId(replyEntity.getCommentId())
                .type(replyEntity.getType())
                .password(replyEntity.getPassword())
                .content(replyEntity.getContent())
                .likeCount(replyEntity.getLikeCount())
                .createdAt(replyEntity.getCreatedAt())
                .updatedAt(replyEntity.getUpdatedAt())
                .version(replyEntity.getVersion())
                .build();
    }
}
