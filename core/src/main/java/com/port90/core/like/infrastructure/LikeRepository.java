package com.port90.core.like.infrastructure;

import com.port90.core.like.domain.model.Like;

public interface LikeRepository {
    Like save(Like like);

    boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    Like findById(Long likeId);

    void deleteById(Long likeId);
}
