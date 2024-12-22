package com.port90.core.comment.infrastructure.impl.repository.persistence;

import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    @Query("select c.id from Comment c where c.parentId = :parentId")
    List<Long> findChildIdsByParentId(@Param("parentId") Long parentId);

    @Modifying
    @Query("delete from Comment c where c.id in :commentIds")
    int deleteAllByIdIn(@Param("commentIds") List<Long> commentIds);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select c from Comment c where c.id = :commentId")
    Optional<CommentEntity> findByIdWithOptimisticLock(@RequestParam("commentId") Long commentId);

    int countByParentId(Long parentId);
}
