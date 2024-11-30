package com.port90.core.auth.infrastructure.impl.repository.persistence;

import com.port90.core.auth.domain.model.FavoriteStock;
import com.port90.core.auth.infrastructure.impl.repository.persistence.entity.FavoriteStockEntity;

public class FavoriteStockMapper {

    public static FavoriteStockEntity toEntity(FavoriteStock favoriteStock) {
        return FavoriteStockEntity.builder()
                .id(favoriteStock.getId())
                .userId(favoriteStock.getUserId())
                .stockCode(favoriteStock.getStockCode())
                .stockName(favoriteStock.getStockName())
                .createdAt(favoriteStock.getCreatedAt())
                .updatedAt(favoriteStock.getUpdatedAt())
                .build();
    }

    public static FavoriteStock toModel(FavoriteStockEntity entity) {
        return FavoriteStock.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .stockCode(entity.getStockCode())
                .stockName(entity.getStockName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
