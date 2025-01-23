package com.port90.core.comment.application;

import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.infrastructure.CommentRepository;
import com.port90.core.stockchart.domain.StockChartMinuteId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.port90.core.comment.domain.model.CommentType.UNAUTHENTICATED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class CommentLikeConcurrencyTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Test
    void 동시에_댓글_좋아요를_증가시킨다() throws InterruptedException {
        //given
        Comment comment = Comment.builder()
                .stockChartMinuteId(
                        StockChartMinuteId.of(
                                "stockCode",
                                LocalDate.of(2025, 1, 21),
                                LocalTime.of(16, 20)
                        )
                )
                .type(UNAUTHENTICATED)
                .password("password")
                .content("content")
                .likeCount(0L)
                .build();
        Comment saved = commentRepository.save(comment);

        int threadCount = 100;

        //when
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    commentService.increaseLikeCount(saved.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        Comment find = commentRepository.getById(saved.getId());
        assertEquals(100L, find.getLikeCount());
    }

    @Test
    void 동시에_댓글_좋아요를_감소시킨다() throws InterruptedException {
        //given
        Comment comment = Comment.builder()
                .stockChartMinuteId(
                        StockChartMinuteId.of(
                                "stockCode",
                                LocalDate.of(2025, 1, 21),
                                LocalTime.of(16, 20)
                        )
                )
                .type(UNAUTHENTICATED)
                .password("password")
                .content("content")
                .likeCount(100L)
                .build();
        Comment saved = commentRepository.save(comment);

        int threadCount = 100;

        //when
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    commentService.decreaseLikeCount(saved.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        Comment find = commentRepository.getById(saved.getId());
        assertEquals(0L, find.getLikeCount());
    }
}