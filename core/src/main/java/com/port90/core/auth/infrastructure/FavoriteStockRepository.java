package com.port90.core.auth.infrastructure;

import com.port90.core.auth.domain.model.FavoriteStock;

import java.util.List;
import java.util.Optional;

public interface FavoriteStockRepository {
    FavoriteStock save(FavoriteStock favoriteStock);

    List<FavoriteStock> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<FavoriteStock> findByUserIdAndStockCode(Long userId, String stockCode);

    void deleteByUserIdAndStockCode(Long userId, String stockCode);
}