package com.port90.core.comment.domain.model;

import com.port90.core.like.domain.exception.LikeException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import static com.port90.core.comment.domain.model.CommentType.*;
import static com.port90.core.like.domain.exception.LikeErrorCode.LIKE_CAN_NOT_BE_LESS_THAN_ZERO;

@Getter
@Builder
public class Comment {

    private Long id;
    private String stockCode;
    private LocalDate date;
    private LocalTime time;
    private Long userId;
    private CommentType type;
    private String password;
    private String content;
    private Long parentId;
    private int likeCount;
    private boolean isParent;
    private boolean isChild;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    public static Comment createUserComment(
            String stockCode, LocalDate date, LocalTime time, Long userId, String content, Long parentId
    ) {
        return Comment.builder()
                .stockCode(stockCode)
                .date(date)
                .time(time)
                .userId(userId)
                .type(USER)
                .content(content)
                .parentId(parentId)
                .build();
    }

    public static Comment createAnonymousUserComment(
            String stockCode, LocalDate date, LocalTime time, Long userId, String content, Long parentId
    ) {
        return Comment.builder()
                .stockCode(stockCode)
                .date(date)
                .time(time)
                .userId(userId)
                .type(ANONYMOUS_USER)
                .content(content)
                .parentId(parentId)
                .build();
    }

    public static Comment createGuestComment(
            String stockCode, LocalDate date, LocalTime time, String content, Long parentId, String password
    ) {
        return Comment.builder()
                .stockCode(stockCode)
                .date(date)
                .time(time)
                .type(GUEST)
                .content(content)
                .parentId(parentId)
                .password(password)
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

    public boolean isGuestComment() {
        return this.type == GUEST;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void hasNotChild() {
        this.isParent = false;
    }

    public boolean isUserComment() {
        return this.type == USER;
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
