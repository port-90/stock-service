package com.port90.core.like.infrastructure;

import com.port90.core.like.domain.model.Like;
import jakarta.validation.constraints.NotNull;

public interface LikeRepository {
    Like save(Like like);

    boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    Like findById(Long likeId);

    void delete(Like like);
}
