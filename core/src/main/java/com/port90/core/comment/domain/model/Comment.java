package com.port90.core.comment.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class Comment {

    private Long id;
    private String stockCode;
    private Long userId;
    private String guestId;
    private String guestPassword;
    private String content;
    private Long parentId;
    private int likeCount;
    private boolean hasChildren;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Comment createByUser(Long userId, String stockCode, String content, Long parentId) {
        return Comment.builder()
                .userId(userId)
                .stockCode(stockCode)
                .content(content)
                .parentId(parentId)
                .likeCount(0)
                .hasChildren(false)
                .build();
    }

    public static Comment createByGuest(String stockCode, String guestId, String guestPassword, String content, Long parentId) {
        return Comment.builder()
                .stockCode(stockCode)
                .guestId(guestId)
                .guestPassword(guestPassword)
                .content(content)
                .parentId(parentId)
                .likeCount(0)
                .hasChildren(false)
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

    public void existChildren() {
        this.hasChildren = true;
    }

    public boolean hasParent() {
        return this.parentId != null;
    }

    public void nonExistChildren() {
        this.hasChildren = false;
    }
}
