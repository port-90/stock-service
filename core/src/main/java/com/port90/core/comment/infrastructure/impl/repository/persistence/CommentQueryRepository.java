package com.port90.core.comment.infrastructure.impl.repository.persistence;

import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.port90.core.comment.infrastructure.impl.repository.persistence.entity.QCommentEntity.commentEntity;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CommentEntity> findParentCommentsByStockCodeByCursor(String stockCode, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), parentIdIsNull(), idLessThan(cursor))
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

    private BooleanExpression stockCodeEq(String stockCode) {
        return stockCode != null ? commentEntity.stockCode.eq(stockCode) : null;
    }

    private BooleanExpression parentIdIsNull() {
        return commentEntity.parentId.isNull();
    }

    private BooleanExpression idLessThan(Long cursor) {
        return cursor != null ? commentEntity.id.lt(cursor) : null;
    }

    private BooleanExpression parentIdEq(Long parentId) {
        return parentId != null ? commentEntity.parentId.eq(parentId) : null;
    }
}
