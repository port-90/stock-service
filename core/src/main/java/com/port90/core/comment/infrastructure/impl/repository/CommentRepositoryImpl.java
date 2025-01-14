package com.port90.core.comment.infrastructure.impl.repository;

import com.port90.core.comment.domain.exception.CommentException;
import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.dto.ChildCommentCountDto;
import com.port90.core.comment.infrastructure.CommentRepository;
import com.port90.core.comment.infrastructure.impl.repository.persistence.CommentJpaRepository;
import com.port90.core.comment.infrastructure.impl.repository.persistence.CommentQueryRepository;
import com.port90.core.comment.infrastructure.impl.repository.persistence.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.port90.core.comment.domain.exception.CommentErrorCode.COMMENT_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Override
    public Comment save(Comment comment) {
        return CommentMapper.toModel(
                commentJpaRepository.save(
                        CommentMapper.toEntity(comment)
                )
        );
    }

    @Override
    public Comment getById(Long commentId) {
        return commentJpaRepository.findById(commentId)
                .map(CommentMapper::toModel)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));
    }

    @Override
    public Comment getByIdWithOptimisticLock(Long commentId) {
        return commentJpaRepository.findByIdWithOptimisticLock(commentId)
                .map(CommentMapper::toModel)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));
    }

    @Override
    public List<Long> findIdsByParentId(Long parentId) {
        return commentJpaRepository.findChildIdsByParentId(parentId);
    }

    @Override
    public int deleteAllByIdIn(List<Long> commentIds) {
        return commentJpaRepository.deleteAllByIdIn(commentIds);
    }

    @Override
    public List<Comment> findParentsByStockCodeByCursor(String stockCode, Long cursor, int size) {
        return commentQueryRepository
                .findByStockCodeByCursor(stockCode, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public List<Comment> findChildrenByParentIdByCursor(Long parentId, Long cursor, int size) {
        return commentQueryRepository
                .findByParentIdByCursor(parentId, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(CommentMapper.toEntity(comment));
    }

    @Override
    public Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable) {
        return commentQueryRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(CommentMapper::toModel);
    }

    @Override
    public List<Comment> findParentsByStockCodeAndDateAndTimeByCursor(String stockCode, LocalDate date, LocalTime time, Long cursor, int size) {
        return commentQueryRepository.findByStockCodeAndDateAndTimeByCursor(stockCode, date, time, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public long countByParentId(Long parentId) {
        return commentJpaRepository.countByParentId(parentId);
    }

    @Override
    public List<Comment> findParentsByStockCodeAndDateAndTimeBetweenByCursor(String stockCode, LocalDate date, LocalTime startTime, LocalTime time, Long cursor, int size) {
        return commentQueryRepository
                .findByStockCodeAndDateAndTimeBetweenByCursor(stockCode, date, startTime, time, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public List<Comment> findParentsByStockCodeAndDateByCursor(String stockCode, LocalDate date, Long cursor, int size) {
        return commentQueryRepository
                .findByStockCodeAndDateByCursor(stockCode, date, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public List<Comment> findParentsByStockCodeAndDateBetweenByCursor(String stockCode, LocalDate startDate, LocalDate endDate, Long cursor, int size) {
        return commentQueryRepository
                .findByStockCodeAndDateBetweenByCursor(stockCode, startDate, endDate, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public List<ChildCommentCountDto> findChildCountsByParentIdIn(List<Long> commentIdList) {
        return commentQueryRepository.findChildCommentCountsByParentIdIn(commentIdList);
    }
}
