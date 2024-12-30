package com.port90.core.comment.infrastructure;

import com.port90.core.comment.domain.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    Comment findById(Long commentId);

    Comment findByIdWithOptimisticLock(Long commentId);

    List<Long> findChildIdsByParentId(Long parentId);

    int deleteAllByIdIn(List<Long> commentIds);

    List<Comment> findByStockCodeByCursor(String stockCode, Long cursor, int size);

    List<Comment> findByParentIdByCursor(Long parentId, Long cursor, int size);

    void delete(Comment comment);

    Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable); // 페이징 지원

    List<Comment> findByStockCodeAndDateAndTimeByCursor(String stockCode, LocalDate date, LocalTime time, Long cursor, int size);

    int countByParentId(Long parentId);

    List<Comment> findByStockCodeAndDateAndTimeBetweenByCursor(String stockCode, LocalDate date, LocalTime startTime, LocalTime time, Long cursor, int size);

    List<Comment> findByStockCodeAndDateByCursor(String stockCode, LocalDate date, Long cursor, int size);

    List<Comment> findByStockCodeAndDateBetweenByCursor(String stockCode, LocalDate startOfWeek, LocalDate endOfWeek, Long cursor, int size);
}
