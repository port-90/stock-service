package com.port90.core.comment.domain.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class GuestComment extends Comment {
    private String password;

    public static GuestComment create(String stockCode, String content, Long parentId, String author, String password) {
        return GuestComment.builder()
                .stockCode(stockCode)
                .content(content)
                .author(author)
                .parentId(parentId)
                .password(password)
                .build();
    }
}
