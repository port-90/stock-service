package com.port90.comment.infrastructure;

import com.port90.comment.domain.model.Comment;

public interface CommentRepository {
    Comment save(Comment comment);

    boolean existsByParentId(Long parentId);
}
