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

    List<Comment> findAllByStockCodeByCursor(String stockCode, Long cursor, int size);

    List<Comment> findAllByParentIdByCursor(Long parentId, Long cursor, int size);

    void delete(Comment comment);

    Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable); // 페이징 지원

    List<Comment> findAllByStockCodeAndDateAndTimeByCursor(String stockCode, LocalDate date, LocalTime time, Long cursor, int size);

    long countByParentId(Long parentId);

    List<Comment> findAllByStockCodeAndDateAndTimeBetweenByCursor(String stockCode, LocalDate date, LocalTime startTime, LocalTime time, Long cursor, int size);

    List<Comment> findAllByStockCodeAndDateByCursor(String stockCode, LocalDate date, Long cursor, int size);

    List<Comment> findAllByStockCodeAndDateBetweenByCursor(String stockCode, LocalDate startOfWeek, LocalDate endOfWeek, Long cursor, int size);

    List<ChildCommentCountDto> findChildCommentCountsByParentIdIn(List<Long> commentIdList);
}
