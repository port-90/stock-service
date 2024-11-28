package com.port90.core.auth.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FavoriteStock {
    private Long id;
    private Long userId;
    private String stockCode;
    private String stockName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FavoriteStock createFavoriteStock(Long userId, String stockCode, String stockName) {
        return FavoriteStock.builder()
                .userId(userId)
                .stockCode(stockCode)
                .stockName(stockName)
                .build();
    }
}
