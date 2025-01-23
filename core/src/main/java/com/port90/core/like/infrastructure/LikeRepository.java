package com.port90.core.like.infrastructure;

import com.port90.core.like.domain.model.Like;
import com.port90.core.like.domain.model.LikeTarget;

public interface LikeRepository {
    Like save(Like like);

    void delete(Like like);

    Like getById(Long likeId);

    boolean existsByUserIdAndTargetAndTargetId(Long userId, LikeTarget target, Long targetId);
}
