package com.port90.core.comment.infrastructure.impl.repository.persistence;

import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    @Query("select c.id from Comment c where c.parentId = :commentId")
    List<Long> findChildIdsByParentId(@Param("commentId") Long commentId);

    @Modifying
    @Query("delete from Comment c where c.id in :commentIds")
    int deleteAllByIdIn(@Param("commentIds") List<Long> commentIds);

    long countByParentId(Long parentId);
}
