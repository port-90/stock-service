package com.port90.core.stockinfo.infrastructure.impl;

import com.port90.stockdomain.domain.info.StockInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.port90.stockdomain.domain.info.QStockInfo.stockInfo;

@Repository
@RequiredArgsConstructor
public class StockInfoQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<StockInfo> findAllByCondition(String stockCode, String stockName, String cursor, int size) {
        return jpaQueryFactory
                .selectFrom(stockInfo)
                .where(stockCodeEq(stockCode), stockNameEq(stockName), cursorGt(cursor))
                .orderBy(stockInfo.stockCode.asc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression stockCodeEq(String stockCode) {
        return stockCode != null ? stockInfo.stockCode.eq(stockCode) : null;
    }

    private BooleanExpression stockNameEq(String stockName) {
        return stockName != null ? stockInfo.stockName.eq(stockName) : null;
    }

    private BooleanExpression cursorGt(String cursor) {
        return cursor != null ? stockInfo.stockCode.gt(cursor) : null;
    }
}
