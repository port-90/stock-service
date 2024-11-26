package com.port90.comment.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Comment {

    private Long id;

    private Long userId;

    private String guestId;

    private String guestPassword;

    private String content;

    private Long parentId;

    private int likeCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Comment createByUser(Long userId, String content, Long parentId) {
        return Comment.builder()
                .userId(userId)
                .content(content)
                .parentId(parentId)
                .likeCount(0)
                .build();
    }

    public static Comment createByGuest(String guestId, String guestPassword, String content, Long parentId) {
        return Comment.builder()
                .guestId(guestId)
                .guestPassword(guestPassword)
                .content(content)
                .parentId(parentId)
                .likeCount(0)
                .build();
    }
}
