package com.port90.core.comment.infrastructure;

import com.port90.core.comment.domain.model.Comment;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    Comment findById(Long commentId);

    Comment findByIdWithOptimisticLock(Long commentId);

    List<Long> findChildIdsByParentId(Long parentId);

    int deleteAllByIdIn(List<Long> commentIds);

    List<Comment> findCommentsByStockCodeByCursor(String stockCode, Long cursor, int size);

    List<Comment> findChildCommentsByParentIdByCursor(Long parentId, Long cursor, int size);

    void delete(Comment comment);
}
