package com.port90.core.reply.domain.model;

import com.port90.core.reply.domain.error.ReplyException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.port90.core.reply.domain.error.ReplyErrorCode.REPLY_LIKE_CAN_NOT_BE_LESS_THAN_ZERO;
import static com.port90.core.reply.domain.error.ReplyErrorCode.REPLY_USER_MISMATCH;
import static com.port90.core.reply.domain.model.ReplyType.*;

@Getter
@Builder
public class Reply {
    private Long id;
    private Long userId;
    private Long commentId;

    private ReplyType type;
    private String password;

    private String content;
    private Long likeCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    public static Reply createUnAuthenticated(ReplyCreate replyCreate, ReplyType type, String password) {

        return Reply.builder()
                .commentId(replyCreate.commentId())
                .type(type)
                .password(password)
                .content(replyCreate.content())
                .likeCount(0L)
                .version(0L)
                .build();
    }

    public static Reply createAuthenticated(ReplyCreate replyCreate, ReplyType type) {
        return Reply.builder()
                .userId(replyCreate.userId())
                .commentId(replyCreate.commentId())
                .type(type)
                .content(replyCreate.content())
                .likeCount(0L)
                .version(0L)
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
            throw new ReplyException(REPLY_USER_MISMATCH);
        }
    }

    public void updateContent(ReplyUpdate replyUpdate) {
        this.content = replyUpdate.content();
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount == 0) {
            throw new ReplyException(REPLY_LIKE_CAN_NOT_BE_LESS_THAN_ZERO);
        }
        this.likeCount--;
    }
}
