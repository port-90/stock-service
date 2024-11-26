package com.port90.core.comment.infrastructure.impl.repository.persistence;

import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
}
