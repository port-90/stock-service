package com.port90.core.comment.domain.model;

import com.port90.core.like.domain.exception.LikeException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.port90.core.like.domain.exception.LikeErrorCode.LIKE_CAN_NOT_BE_LESS_THAN_ZERO;

@Getter
@Builder
public class Comment {

    private Long id;
    private String stockCode;
    private Long userId;
    private String guestPassword;
    private String content;
    private Long parentId;
    private int likeCount;
    private boolean isParent;
    private boolean isChild;
    private boolean isUserComment;
    private boolean isAnonymousUserComment;
    private boolean isGuestComment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Comment createByUser(Long userId, String stockCode, String content, Long parentId) {
        return Comment.builder()
                .userId(userId)
                .stockCode(stockCode)
                .content(content)
                .parentId(parentId)
                .likeCount(0)
                .isUserComment(true)
                .build();
    }

    public static Comment createByAnonymousUser(Long userId, String stockCode, String content, Long parentId) {
        return Comment.builder()
                .userId(userId)
                .stockCode(stockCode)
                .content(content)
                .parentId(parentId)
                .likeCount(0)
                .isAnonymousUserComment(true)
                .build();
    }

    public static Comment createByGuest(String stockCode, String guestPassword, String content, Long parentId) {
        return Comment.builder()
                .stockCode(stockCode)
                .guestPassword(guestPassword)
                .content(content)
                .parentId(parentId)
                .likeCount(0)
                .isGuestComment(true)
                .build();
    }

    public void hasChild() {
        this.isParent = true;
    }

    public void hasParent() {
        this.isChild = true;
    }

    public boolean isNotWrittenBy(Long userId) {
        return !Objects.equals(this.userId, userId);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void isNotParent() {
        this.isParent = false;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount == 0) {
            throw new LikeException(LIKE_CAN_NOT_BE_LESS_THAN_ZERO);
        }
        this.likeCount--;
    }
}
