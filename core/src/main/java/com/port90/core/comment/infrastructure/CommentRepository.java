package com.port90.core.comment.infrastructure;

import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.dto.ChildCommentCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    Comment getById(Long commentId);

    Comment getByIdWithOptimisticLock(Long commentId);

    List<Long> findIdsByParentId(Long parentId);

    int deleteAllByIdIn(List<Long> commentIds);

    List<Comment> findChildrenByParentIdByCursor(Long parentId, Long cursor, int size);

    void delete(Comment comment);

    Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable); // 페이징 지원

    long countByParentId(Long parentId);

    List<Comment> findParentsByStockCodeByCursor(String stockCode, Long cursor, int size);

    List<Comment> findParentsByStockCodeAndDateAndTimeByCursor(String stockCode, LocalDate date, LocalTime time, Long cursor, int size);

    List<Comment> findParentsByStockCodeAndDateAndTimeBetweenByCursor(String stockCode, LocalDate date, LocalTime startTime, LocalTime time, Long cursor, int size);

    List<Comment> findParentsByStockCodeAndDateByCursor(String stockCode, LocalDate date, Long cursor, int size);

    List<Comment> findParentsByStockCodeAndDateBetweenByCursor(String stockCode, LocalDate startDate, LocalDate endDate, Long cursor, int size);

    List<ChildCommentCountDto> findChildCountsByParentIdIn(List<Long> commentIdList);
}
