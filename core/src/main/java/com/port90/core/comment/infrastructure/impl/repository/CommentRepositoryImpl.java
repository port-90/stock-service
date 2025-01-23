package com.port90.core.comment.infrastructure.impl.repository;

import com.port90.core.comment.domain.error.CommentException;
import com.port90.core.comment.domain.model.Comment;
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

import static com.port90.core.comment.domain.error.CommentErrorCode.COMMENT_NOT_FOUND_BY_ID;

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
    public Comment saveAndFlush(Comment comment) {
        return CommentMapper.toModel(
                commentJpaRepository.saveAndFlush(
                        CommentMapper.toEntity(comment)
                )
        );
    }

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(CommentMapper.toEntity(comment));
    }

    @Override
    public boolean existsById(Long commentId) {
        return commentJpaRepository.existsById(commentId);
    }

    @Override
    public Comment getById(Long commentId) {
        return commentJpaRepository.findById(commentId)
                .map(CommentMapper::toModel)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND_BY_ID, commentId));
    }

    @Override
    public Page<Comment> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable) {
        return commentJpaRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(CommentMapper::toModel);
    }

    @Override
    public List<Comment> findAllByStockCodeByCursor(String stockCode, Long cursor, int size) {
        return commentQueryRepository
                .findAllByStockCodeByCursor(stockCode, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public List<Comment> findAllByStockCodeAndDateAndTimeByCursor(String stockCode, LocalDate date, LocalTime time, Long cursor, int size) {
        return commentQueryRepository.findAllByStockCodeAndDateAndTimeByCursor(stockCode, date, time, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public List<Comment> findAllByStockCodeAndDateAndTimeBetweenByCursor(String stockCode, LocalDate date, LocalTime startTime, LocalTime endTime, Long cursor, int size) {
        return commentQueryRepository
                .findAllByStockCodeAndDateAndTimeBetweenByCursor(stockCode, date, startTime, endTime, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public List<Comment> findAllByStockCodeAndDateByCursor(String stockCode, LocalDate date, Long cursor, int size) {
        return commentQueryRepository
                .findAllByStockCodeAndDateByCursor(stockCode, date, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public List<Comment> findAllByStockCodeAndDateBetweenByCursor(String stockCode, LocalDate startDate, LocalDate endDate, Long cursor, int size) {
        return commentQueryRepository
                .findAllByStockCodeAndDateBetweenByCursor(stockCode, startDate, endDate, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }
}
