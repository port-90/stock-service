package com.port90.core.comment.infrastructure;

import com.port90.core.comment.domain.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    Comment saveAndFlush(Comment comment);

    void delete(Comment comment);

    boolean existsById(Long commentId);

    Comment getById(Long commentId);

    Page<Comment> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable); // 페이징 지원

    List<Comment> findAllByStockCodeByCursor(String stockCode, Long cursor, int size);

    List<Comment> findAllByStockCodeAndDateAndTimeByCursor(String stockCode, LocalDate date, LocalTime time, Long cursor, int size);

    List<Comment> findAllByStockCodeAndDateAndTimeBetweenByCursor(String stockCode, LocalDate date, LocalTime startTime, LocalTime endTime, Long cursor, int size);

    List<Comment> findAllByStockCodeAndDateByCursor(String stockCode, LocalDate date, Long cursor, int size);

    List<Comment> findAllByStockCodeAndDateBetweenByCursor(String stockCode, LocalDate startDate, LocalDate endDate, Long cursor, int size);
}
