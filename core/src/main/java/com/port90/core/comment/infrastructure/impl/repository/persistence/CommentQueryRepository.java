package com.port90.core.comment.infrastructure.impl.repository.persistence;

import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;
import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.QUserCommentEntity;
import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.UserCommentEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.port90.core.comment.infrastructure.impl.repository.persistence.entity.QCommentEntity.commentEntity;
import static com.querydsl.jpa.JPAExpressions.treat;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CommentEntity> findCommentsByStockCodeByCursor(String stockCode, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), parentIdIsNull(), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }

    public List<CommentEntity> findCommentsByStockCodeByCursorBetween(String stockCode, Long cursor, int size, LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), parentIdIsNull(), idLessThan(cursor), createdAtBetween(start, end))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }

    public List<CommentEntity> findChildCommentsByParentIdByCursor(Long parentId, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(parentIdEq(parentId), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }

    public Page<CommentEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable) {
        List<CommentEntity> comments = jpaQueryFactory
                .selectFrom(commentEntity)
                .where(userIdEq(userId))
                .orderBy(commentEntity.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(commentEntity.count())
                .from(commentEntity)
                .where(userIdEq(userId))
                .fetchOne();

        return new PageImpl<>(comments, pageable, total);
    }

    private BooleanExpression stockCodeEq(String stockCode) {
        return stockCode != null ? commentEntity.stockCode.eq(stockCode) : null;
    }

    private BooleanExpression parentIdIsNull() {
        return commentEntity.parentId.isNull();
    }

    private BooleanExpression idLessThan(Long cursor) {
        return cursor != null ? commentEntity.id.lt(cursor) : null;
    }

    private BooleanExpression createdAtBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return null;
        }
        return commentEntity.createdAt.between(start, end);
    }

    private BooleanExpression parentIdEq(Long parentId) {
        return parentId != null ? commentEntity.parentId.eq(parentId) : null;
    }

    private BooleanExpression userIdEq(Long userId) {
        return commentEntity.instanceOf(UserCommentEntity.class)
                .and(treat(commentEntity, QUserCommentEntity.class).userId.eq(userId));
    }
}
