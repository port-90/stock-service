package com.port90.core.reply.infrastructure.impl.repository.persistence;

import com.port90.core.reply.domain.model.CommentReplyCount;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static com.port90.core.reply.infrastructure.impl.repository.persistence.entity.QReplyEntity.replyEntity;

@Repository
@RequiredArgsConstructor
public class ReplyQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CommentReplyCount> getCommentReplyCountList(Set<Long> commentIds) {
        return jpaQueryFactory
                .select(Projections.constructor(CommentReplyCount.class,
                        replyEntity.commentId,
                        replyEntity.count()
                ))
                .from(replyEntity)
                .where(replyEntity.commentId.in(commentIds))
                .groupBy(replyEntity.commentId)
                .fetch();
    }
}
