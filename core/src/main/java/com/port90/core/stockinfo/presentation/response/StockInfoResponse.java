package com.port90.core.stockinfo.presentation.response;

import com.port90.core.stockinfo.domain.StockInfoDto;
import com.port90.stockdomain.domain.info.StockInfoStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record StockInfoResponse(
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
    public static List<StockInfoResponse> from(List<StockInfoDto> stockInfos) {
        return stockInfos.stream()
                .map(StockInfoResponse::from)
                .toList();
    }

    private static StockInfoResponse from(StockInfoDto stockInfo) {
        return StockInfoResponse.builder()
                .stockCode(stockInfo.stockCode())
                .stockName(stockInfo.stockName())
                .stockCount(stockInfo.stockCount())
                .marketCap(stockInfo.marketCap())
                .market(stockInfo.market())
                .stockKind(stockInfo.stockKind())
                .stopStatus(stockInfo.stopStatus())
                .lockStatus(stockInfo.lockStatus())
                .status(stockInfo.status())
                .openDate(stockInfo.openDate())
                .closeDate(stockInfo.closeDate())
                .createdAt(stockInfo.createdAt())
                .updatedAt(stockInfo.updatedAt())
                .build();
    }
}
