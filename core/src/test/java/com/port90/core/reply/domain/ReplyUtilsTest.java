package com.port90.core.reply.domain;

import com.port90.core.reply.domain.model.ReplyCreate;
import com.port90.core.reply.domain.model.ReplyType;
import com.port90.core.reply.domain.model.ReplyUtils;
import org.junit.jupiter.api.Test;

import static com.port90.core.reply.domain.model.ReplyType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReplyUtilsTest {

    @Test
    void ReplyCreate로_UNAUTHENTICATED_ReplyType을_결정할_수_있다() {
        //given
        ReplyCreate replyCreate = ReplyCreate.builder()
                .userId(null)
                .build();

        //when
        ReplyType replyType = ReplyUtils.resolveType(replyCreate);

        //then
        assertEquals(UNAUTHENTICATED, replyType);
    }

    @Test
    void ReplyCreate로_AUTHENTICATED_ReplyType을_결정할_수_있다() {
        //given
        ReplyCreate replyCreate1 = ReplyCreate.builder()
                .userId(1L)
                .isAnonymous(false)
                .build();
        ReplyCreate replyCreate2 = ReplyCreate.builder()
                .userId(1L)
                .isAnonymous(null)
                .build();

        //when
        ReplyType replyType1 = ReplyUtils.resolveType(replyCreate1);
        ReplyType replyType2 = ReplyUtils.resolveType(replyCreate2);

        //then
        assertEquals(AUTHENTICATED, replyType1);
        assertEquals(AUTHENTICATED, replyType2);
    }

    @Test
    void ReplyCreate로_AUTHENTICATED_ANONYMOUS_ReplyType을_결정할_수_있다() {
        //given
        ReplyCreate replyCreate = ReplyCreate.builder()
                .userId(1L)
                .isAnonymous(true)
                .build();

        //when
        ReplyType replyType = ReplyUtils.resolveType(replyCreate);

        //then
        assertEquals(AUTHENTICATED_ANONYMOUS, replyType);
    }
}