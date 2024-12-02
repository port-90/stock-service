package com.port90.core.comment.infrastructure.impl.repository;

import com.port90.core.comment.domain.exception.CommentException;
import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.infrastructure.CommentRepository;
import com.port90.core.comment.infrastructure.impl.repository.persistence.CommentJpaRepository;
import com.port90.core.comment.infrastructure.impl.repository.persistence.CommentQueryRepository;
import com.port90.core.comment.infrastructure.impl.repository.persistence.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.port90.core.comment.domain.exception.ErrorCode.COMMENT_NOT_FOUND;

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
    public boolean existsByParentId(Long parentId) {
        return commentJpaRepository.existsById(parentId);
    }

    @Override
    public Comment findById(Long commentId) {
        return CommentMapper.toModel(
                commentJpaRepository.findById(commentId)
                        .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND))
        );
    }

    @Override
    public List<Long> findChildIdsByParentId(Long commentId) {
        return commentJpaRepository.findChildIdsByParentId(commentId);
    }

    @Override
    public int deleteAllByIdIn(List<Long> commentIds) {
        return commentJpaRepository.deleteAllByIdIn(commentIds);
    }

    @Override
    public List<Comment> findParentCommentsByStockCodeByCursor(String stockCode, Long cursor, int size) {
        return commentQueryRepository
                .findParentCommentsByStockCodeByCursor(stockCode, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public List<Comment> findChildCommentsByParentIdByCursor(Long parentId, Long cursor, int size) {
        return commentQueryRepository
                .findChildCommentsByParentIdByCursor(parentId, cursor, size)
                .stream()
                .map(CommentMapper::toModel)
                .toList();
    }

    @Override
    public long countByParentId(Long parentId) {
        return commentJpaRepository.countByParentId(parentId);
    }
}
