package com.port90.core.comment.infrastructure;

import com.port90.core.comment.domain.model.Comment;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    boolean existsByParentId(Long parentId);

    Comment findById(Long commentId);

    List<Long> findChildIdsByParentId(Long commentId);

    int deleteAllByIdIn(List<Long> commentIds);

    List<Comment> findCommentsByStockCodeByCursor(String stockCode, Long cursor, int size);
}
