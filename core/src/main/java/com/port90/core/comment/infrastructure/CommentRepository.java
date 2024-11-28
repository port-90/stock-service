package com.port90.core.comment.infrastructure;

import com.port90.core.comment.domain.model.Comment;

public interface CommentRepository {
    Comment save(Comment comment);

    boolean existsByParentId(Long parentId);

    Comment findById(Long commentId);
}
