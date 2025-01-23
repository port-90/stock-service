package com.port90.core.reply.infrastructure.impl.repository.persistence;

import com.port90.core.reply.infrastructure.impl.repository.persistence.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyJpaRepository extends JpaRepository<ReplyEntity, Long> {

    @Modifying
    @Query("delete from Reply r where r.commentId = :commentId")
    int deleteByCommentId(@Param("commentId") Long commentId);

    List<ReplyEntity> findAllByCommentId(Long commentId);
}
