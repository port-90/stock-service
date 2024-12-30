package com.port90.core.comment.infrastructure.impl.repository.persistence;

import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.port90.core.comment.infrastructure.impl.repository.persistence.entity.QCommentEntity.commentEntity;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CommentEntity> findByStockCodeByCursor(String stockCode, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), parentIdIsNull(), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }

    public List<CommentEntity> findByStockCodeAndDateAndTimeByCursor(String stockCode, LocalDate date, LocalTime time, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), dateEq(date), timeEq(time), parentIdIsNull(), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }


    public List<CommentEntity> findByStockCodeAndDateAndTimeBetweenByCursor(String stockCode, LocalDate date, LocalTime startTime, LocalTime time, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), dateEq(date), timeGt(startTime), timeLoe(time), parentIdIsNull(), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }

    public List<CommentEntity> findByStockCodeAndDateByCursor(String stockCode, LocalDate date, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), dateEq(date), parentIdIsNull(), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }

    public List<CommentEntity> findByStockCodeAndDateBetweenByCursor(String stockCode, LocalDate startOfWeek, LocalDate endOfWeek, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), dateBetween(startOfWeek, endOfWeek), parentIdIsNull(), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }

    public List<CommentEntity> findByParentIdByCursor(Long parentId, Long cursor, int size) {
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
        return commentEntity.stockCode.eq(stockCode);
    }

    private BooleanExpression dateEq(LocalDate date) {
        return commentEntity.date.eq(date);
    }

    private BooleanExpression dateBetween(LocalDate startOfWeek, LocalDate endOfWeek) {
        return commentEntity.date.between(startOfWeek, endOfWeek);
    }

    private BooleanExpression timeEq(LocalTime time) {
        return commentEntity.time.eq(time);
    }

    private BooleanExpression timeGt(LocalTime time) {
        return commentEntity.time.gt(time);
    }

    private BooleanExpression timeLoe(LocalTime time) {
        return commentEntity.time.loe(time);
    }

    private BooleanExpression parentIdIsNull() {
        return commentEntity.parentId.isNull();
    }

    private BooleanExpression idLessThan(Long cursor) {
        return cursor != null ? commentEntity.id.lt(cursor) : null;
    }

    private BooleanExpression parentIdEq(Long parentId) {
        return commentEntity.parentId.eq(parentId);
    }

    private BooleanExpression userIdEq(Long userId) {
        if (userId == null) {
            throw new RuntimeException("UserId is null");
        }
        return commentEntity.userId.eq(userId);
    }
}
