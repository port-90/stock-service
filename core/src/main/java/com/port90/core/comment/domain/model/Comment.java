package com.port90.core.comment.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public boolean isUserComment() {
        return this.userId != null && this.guestId == null;
    }

    public boolean isNotCreatedBy(Long userId) {
        return !Objects.equals(this.userId, userId);
    }

    public boolean isGuestComment() {
        return this.userId == null && (this.guestId != null && this.guestPassword != null);
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
