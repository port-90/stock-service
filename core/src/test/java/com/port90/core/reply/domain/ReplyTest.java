package com.port90.core.reply.domain;

import com.port90.core.reply.domain.model.Reply;
import com.port90.core.reply.domain.model.ReplyCreate;
import com.port90.core.reply.domain.model.ReplyType;
import org.junit.jupiter.api.Test;

import static com.port90.core.reply.domain.model.ReplyType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReplyTest {

    @Test
    void UNAUTHENTICATED_Reply를_생성할_수_있다() {
        // given
        ReplyCreate replyCreate = ReplyCreate.builder()
                .commentId(1L)
                .content("content")
                .build();
        ReplyType replyType = UNAUTHENTICATED;
        String password = "password";

        // when
        Reply reply = Reply.createUnAuthenticated(replyCreate, replyType, password);

        // then
        assertNull(reply.getUserId());
        assertEquals(1L, reply.getCommentId());
        assertEquals(UNAUTHENTICATED, reply.getType());
        assertEquals("password", reply.getPassword());
        assertEquals("content", reply.getContent());
        assertEquals(0L, reply.getLikeCount());
        assertEquals(0L, reply.getVersion());
    }

    @Test
    void AUTHENTICATED_Reply를_생성할_수_있다() {
        // given
        ReplyCreate replyCreate = ReplyCreate.builder()
                .userId(1L)
                .commentId(1L)
                .content("content")
                .build();
        ReplyType replyType = AUTHENTICATED;

        // when
        Reply reply = Reply.createAuthenticated(replyCreate, replyType);

        // then
        assertEquals(1L, reply.getUserId());
        assertEquals(1L, reply.getCommentId());
        assertEquals(AUTHENTICATED, reply.getType());
        assertNull(reply.getPassword());
        assertEquals("content", reply.getContent());
        assertEquals(0L, reply.getLikeCount());
        assertEquals(0L, reply.getVersion());
    }

    @Test
    void AUTHENTICATED_ANONYMOUS_Reply를_생성할_수_있다() {
        // given
        ReplyCreate replyCreate = ReplyCreate.builder()
                .userId(1L)
                .commentId(1L)
                .content("content")
                .build();
        ReplyType replyType = AUTHENTICATED_ANONYMOUS;

        // when
        Reply reply = Reply.createAuthenticated(replyCreate, replyType);

        // then
        assertEquals(1L, reply.getUserId());
        assertEquals(1L, reply.getCommentId());
        assertEquals(AUTHENTICATED_ANONYMOUS, reply.getType());
        assertNull(reply.getPassword());
        assertEquals("content", reply.getContent());
        assertEquals(0L, reply.getLikeCount());
        assertEquals(0L, reply.getVersion());
    }
}