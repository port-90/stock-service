package com.port90.core.auth.infrastructure.impl.repository.persistence;

import com.port90.core.auth.infrastructure.impl.repository.persistence.entity.FavoriteStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteStockJpaRepository extends JpaRepository<FavoriteStockEntity, Long> {
    List<FavoriteStockEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<FavoriteStockEntity> findByUserIdAndStockCode(Long userId, String stockCode);

    void deleteByUserIdAndStockCode(Long userId, String stockCode);
}
