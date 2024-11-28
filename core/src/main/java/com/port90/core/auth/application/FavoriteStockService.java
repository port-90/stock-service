package com.port90.core.auth.application;

import com.port90.core.auth.domain.exception.ErrorCode;
import com.port90.core.auth.domain.exception.UserException;
import com.port90.core.auth.domain.model.FavoriteStock;
import com.port90.core.auth.dto.request.FavoriteStockRequest;
import com.port90.core.auth.dto.response.FavoriteStockResponse;
import com.port90.core.auth.infrastructure.FavoriteStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteStockService {

    private final FavoriteStockRepository favoriteStockRepository;

    // 관심 주식 추가
    public FavoriteStockResponse addFavoriteStock(Long userId, FavoriteStockRequest request) {
        favoriteStockRepository.findByUserIdAndStockCode(userId, request.getStockCode())
                .ifPresent(existing -> {
                    throw new UserException(ErrorCode.ALREADY_ADDED_FAVORITE_STOCK);
                });

        FavoriteStock favoriteStock = FavoriteStock.createFavoriteStock(userId, request.getStockCode(), request.getStockName());
        favoriteStockRepository.save(favoriteStock);

        return FavoriteStockResponse.builder()
                .stockCode(favoriteStock.getStockCode())
                .stockName(favoriteStock.getStockName())
                .createdAt(favoriteStock.getCreatedAt())
                .build();
    }

    // 관심 주식 조회
    public List<FavoriteStockResponse> getFavoriteStocks(Long userId) {
        List<FavoriteStock> favoriteStocks = favoriteStockRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return favoriteStocks.stream()
                .map(stock -> FavoriteStockResponse.builder()
                        .stockCode(stock.getStockCode())
                        .stockName(stock.getStockName())
                        .createdAt(stock.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavoriteStock(Long userId, String stockCode) {
        favoriteStockRepository.findByUserIdAndStockCode(userId, stockCode)
                .orElseThrow(() -> new UserException(ErrorCode.STOCK_NOT_FOUND));

        favoriteStockRepository.deleteByUserIdAndStockCode(userId, stockCode);
    }
}