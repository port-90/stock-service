package com.port90.core.stockinfo.domain;

import com.port90.stockdomain.domain.info.StockInfo;
import com.port90.stockdomain.domain.info.StockInfoStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StockInfoDto(
        String stockCode,
        String stockName,
        long stockCount, // 상장주식수
        long marketCap, // 시가총액
        String market, // 코스피, 코스닥
        String stockKind, // 그룹코드
        String stopStatus, // 거래정지여부
        String lockStatus, // 락 구분

        StockInfoStatus status,
        LocalDate openDate,
        LocalDate closeDate,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static StockInfoDto from(StockInfo stockInfo) {
        return new StockInfoDto(
                stockInfo.getStockCode(),
                stockInfo.getStockName(),
                stockInfo.getStockCount(),
                stockInfo.getMarketCap(),
                stockInfo.getMarket(),
                stockInfo.getStockKind(),
                stockInfo.getStopStatus(),
                stockInfo.getLockStatus(),
                stockInfo.getStatus(),
                stockInfo.getOpenDate(),
                stockInfo.getCloseDate(),
                stockInfo.getCreatedAt(),
                stockInfo.getUpdatedAt()
        );
    }
}
