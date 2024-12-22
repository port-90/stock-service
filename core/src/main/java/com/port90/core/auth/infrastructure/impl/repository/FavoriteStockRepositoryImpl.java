package com.port90.core.auth.infrastructure.impl.repository;

import com.port90.core.auth.domain.model.FavoriteStock;
import com.port90.core.auth.infrastructure.FavoriteStockRepository;
import com.port90.core.auth.infrastructure.impl.repository.persistence.FavoriteStockJpaRepository;
import com.port90.core.auth.infrastructure.impl.repository.persistence.FavoriteStockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FavoriteStockRepositoryImpl implements FavoriteStockRepository {
    private final FavoriteStockJpaRepository favoriteStockJpaRepository;

    @Override
    public FavoriteStock save(FavoriteStock favoriteStock) {
        return FavoriteStockMapper.toModel(
                favoriteStockJpaRepository.save(FavoriteStockMapper.toEntity(favoriteStock))
        );
    }

    @Override
    public List<FavoriteStock> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return favoriteStockJpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(FavoriteStockMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FavoriteStock> findByUserIdAndStockCode(Long userId, String stockCode) {
        return favoriteStockJpaRepository.findByUserIdAndStockCode(userId, stockCode)
                .map(FavoriteStockMapper::toModel);
    }

    @Override
    public void deleteByUserIdAndStockCode(Long userId, String stockCode) {
        favoriteStockJpaRepository.deleteByUserIdAndStockCode(userId, stockCode);
    }
}
