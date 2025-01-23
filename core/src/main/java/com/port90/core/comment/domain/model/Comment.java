package com.port90.core.comment.domain.model;

import com.port90.core.comment.domain.error.CommentException;
import com.port90.core.stockchart.domain.StockChartMinuteId;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.port90.core.comment.domain.error.CommentErrorCode.COMMENT_LIKE_CAN_NOT_BE_LESS_THAN_ZERO;
import static com.port90.core.comment.domain.error.CommentErrorCode.COMMENT_USER_MISMATCH;
import static com.port90.core.comment.domain.model.CommentType.*;

@Getter
@Builder
public class Comment {

    private Long id;
    private StockChartMinuteId stockChartMinuteId;
    private Long userId;

    private CommentType type;
    private String password;

    private String content;
    private Long likeCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    public static Comment createUnAuthenticated(CommentCreate commentCreate, CommentType type, String password, StockChartMinuteId stockChartMinuteId) {
        return Comment.builder()
                .stockChartMinuteId(stockChartMinuteId)
                .type(type)
                .password(password)
                .content(commentCreate.content())
                .likeCount(0L)
                .build();
    }

    public static Comment createAuthenticated(CommentCreate commentCreate, CommentType type, StockChartMinuteId stockChartMinuteId) {
        return Comment.builder()
                .stockChartMinuteId(stockChartMinuteId)
                .userId(commentCreate.userId())
                .type(type)
                .content(commentCreate.content())
                .likeCount(0L)
                .build();
    }

    public boolean isUnauthenticated() {
        return this.type == UNAUTHENTICATED;
    }

    public boolean isAuthenticated() {
        return this.type == AUTHENTICATED;
    }

    public boolean isAuthenticatedAnonymous() {
        return this.type == AUTHENTICATED_ANONYMOUS;
    }

    public void validateUserId(Long userId) {
        if (!Objects.equals(this.userId, userId)) {
            throw new CommentException(COMMENT_USER_MISMATCH);
        }
    }

    public void updateContent(CommentUpdate commentUpdate) {
        this.content = commentUpdate.content();
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount == 0) {
            throw new CommentException(COMMENT_LIKE_CAN_NOT_BE_LESS_THAN_ZERO);
        }
        this.likeCount--;
    }
}
