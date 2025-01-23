package com.port90.core.reply.application;

import com.port90.core.reply.domain.model.Reply;
import com.port90.core.reply.domain.model.ReplyType;
import com.port90.core.reply.infrastructure.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class ReplyLikeConcurrencyTest {

    @Autowired
    ReplyService replyService;

    @Autowired
    ReplyRepository replyRepository;

    @Test
    void 동시에_답글_좋아요를_증가시킨다() throws InterruptedException {
        //given
        Reply reply = Reply.builder()
                .commentId(1L)
                .type(ReplyType.UNAUTHENTICATED)
                .password("password")
                .content("content")
                .likeCount(0L)
                .build();
        Reply saved = replyRepository.save(reply);

        int threadCount = 100;

        //when
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    replyService.increaseLikeCount(saved.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        Reply find = replyRepository.getById(saved.getId());
        assertEquals(100L, find.getLikeCount());
    }

    @Test
    void 동시에_답글_좋아요를_감소시킨다() throws InterruptedException {
        //given
        Reply reply = Reply.builder()
                .commentId(1L)
                .type(ReplyType.UNAUTHENTICATED)
                .password("password")
                .content("content")
                .likeCount(100L)
                .build();
        Reply saved = replyRepository.save(reply);

        int threadCount = 100;

        //when
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    replyService.decreaseLikeCount(saved.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        Reply find = replyRepository.getById(saved.getId());
        assertEquals(0L, find.getLikeCount());
    }
}