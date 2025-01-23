package com.port90.core.comment.infrastructure.impl.repository.persistence;

import com.port90.core.comment.infrastructure.impl.repository.persistence.entity.CommentEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.port90.core.comment.infrastructure.impl.repository.persistence.entity.QCommentEntity.commentEntity;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CommentEntity> findAllByStockCodeByCursor(String stockCode, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }

    public List<CommentEntity> findAllByStockCodeAndDateAndTimeByCursor(String stockCode, LocalDate date, LocalTime time, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), dateEq(date), timeEq(time), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }


    public List<CommentEntity> findAllByStockCodeAndDateAndTimeBetweenByCursor(String stockCode, LocalDate date, LocalTime startTime, LocalTime endTime, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), dateEq(date), timeGt(startTime), timeLoe(endTime), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }

    public List<CommentEntity> findAllByStockCodeAndDateByCursor(String stockCode, LocalDate date, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), dateEq(date), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
    }

    public List<CommentEntity> findAllByStockCodeAndDateBetweenByCursor(String stockCode, LocalDate startDate, LocalDate endDate, Long cursor, int size) {
        return jpaQueryFactory.selectFrom(commentEntity)
                .where(stockCodeEq(stockCode), dateBetween(startDate, endDate), idLessThan(cursor))
                .orderBy(commentEntity.id.desc())
                .limit(size)
                .fetch();
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

    private BooleanExpression idLessThan(Long cursor) {
        return cursor != null ? commentEntity.id.lt(cursor) : null;
    }
}
