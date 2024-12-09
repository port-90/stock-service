package com.port90.core.comment.domain.model;

import com.port90.core.like.domain.exception.LikeException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.port90.core.like.domain.exception.LikeErrorCode.LIKE_CAN_NOT_BE_LESS_THAN_ZERO;

@Getter
@SuperBuilder
public abstract class Comment {

    private Long id;
    private String stockCode;
    private String content;
    private String author;
    private Long parentId;
    private int likeCount;
    private boolean isParent;
    private boolean isChild;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void hasChild() {
        this.isParent = true;
    }

    public void hasParent() {
        this.isChild = true;
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
