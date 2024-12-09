package com.port90.core.comment.domain.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@SuperBuilder
public class UserComment extends Comment {
    private Long userId;
    private boolean isAnonymous;

    public static UserComment create(String stockCode, String content, String author, Long parentId, Long userId, boolean isAnonymous) {
        return UserComment.builder()
                .stockCode(stockCode)
                .content(content)
                .author(author)
                .parentId(parentId)
                .userId(userId)
                .isAnonymous(isAnonymous)
                .build();
    }

    public boolean isNotWrittenBy(Long userId) {
        return !Objects.equals(this.userId, userId);
    }
}
