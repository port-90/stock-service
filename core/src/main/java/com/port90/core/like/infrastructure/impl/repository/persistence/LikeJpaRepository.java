package com.port90.core.like.infrastructure.impl.repository.persistence;

import com.port90.core.like.domain.model.LikeTarget;
import com.port90.core.like.infrastructure.impl.repository.persistence.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {
    boolean existsByUserIdAndTargetAndTargetId(Long userId, LikeTarget target, Long targetId);
}
