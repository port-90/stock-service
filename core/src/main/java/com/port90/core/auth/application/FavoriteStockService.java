package com.port90.core.auth.application;

import com.port90.core.auth.domain.exception.ErrorCode;
import com.port90.core.auth.domain.exception.UserException;
import com.port90.core.auth.domain.model.FavoriteStock;
import com.port90.core.auth.dto.request.FavoriteStockRequest;
import com.port90.core.auth.dto.response.FavoriteStockResponse;
import com.port90.core.auth.infrastructure.FavoriteStockRepository;
import com.port90.stockdomain.domain.chart.StockChartMinute;
import com.port90.stockdomain.infrastructure.StockChartMinuteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteStockService {

    private final FavoriteStockRepository favoriteStockRepository;
    private final StockChartMinuteRepository stockChartMinuteRepository;

    // 관심 주식 추가
    public FavoriteStockResponse addFavoriteStock(Long userId, FavoriteStockRequest request) {
        favoriteStockRepository.findByUserIdAndStockCode(userId, request.getStockCode())
                .ifPresent(existing -> {
                    throw new UserException(ErrorCode.ALREADY_ADDED_FAVORITE_STOCK);
                });

        FavoriteStock favoriteStock = FavoriteStock.createFavoriteStock(userId, request.getStockCode(), request.getStockName());
        favoriteStockRepository.save(favoriteStock);

        StockChartMinute stockChartMinute = stockChartMinuteRepository
                .findFirstByStockCodeOrderByDateDescTimeDesc(request.getStockCode())
                .orElse(null);

        return FavoriteStockResponse.builder()
                .stockCode(favoriteStock.getStockCode())
                .stockName(favoriteStock.getStockName())
                .createdAt(favoriteStock.getCreatedAt())
                // StockChartMinute 정보 추가
                .price(stockChartMinute != null ? stockChartMinute.getPrice() : null)
                .startPrice(stockChartMinute != null ? stockChartMinute.getStartPrice() : null)
                .highPrice(stockChartMinute != null ? stockChartMinute.getHighPrice() : null)
                .lowPrice(stockChartMinute != null ? stockChartMinute.getLowPrice() : null)
                .tradingVolume(stockChartMinute != null ? stockChartMinute.getTradingVolume() : null)
                .tradingValue(stockChartMinute != null ? stockChartMinute.getTradingValue() : null)
                .date(stockChartMinute != null ? stockChartMinute.getDate() : null)
                .time(stockChartMinute != null ? stockChartMinute.getTime() : null)
                .build();
    }

    // 관심 주식 조회
    public List<FavoriteStockResponse> getFavoriteStocks(Long userId) {
        List<FavoriteStock> favoriteStocks = favoriteStockRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return favoriteStocks.stream()
                .map(stock -> {
                    // StockChartMinute 조회
                    StockChartMinute stockChartMinute = stockChartMinuteRepository.findFirstByStockCodeOrderByDateDescTimeDesc(stock.getStockCode())
                            .orElse(null);

                    // FavoriteStockResponse 빌드
                    return FavoriteStockResponse.builder()
                            .stockCode(stock.getStockCode())
                            .stockName(stock.getStockName())
                            .createdAt(stock.getCreatedAt())
                            .price(stockChartMinute != null ? stockChartMinute.getPrice() : null)
                            .startPrice(stockChartMinute != null ? stockChartMinute.getStartPrice() : null)
                            .highPrice(stockChartMinute != null ? stockChartMinute.getHighPrice() : null)
                            .lowPrice(stockChartMinute != null ? stockChartMinute.getLowPrice() : null)
                            .tradingVolume(stockChartMinute != null ? stockChartMinute.getTradingVolume() : null)
                            .tradingValue(stockChartMinute != null ? stockChartMinute.getTradingValue() : null)
                            .date(stockChartMinute != null ? stockChartMinute.getDate() : null)
                            .time(stockChartMinute != null ? stockChartMinute.getTime() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavoriteStock(Long userId, String stockCode) {
        favoriteStockRepository.findByUserIdAndStockCode(userId, stockCode)
                .orElseThrow(() -> new UserException(ErrorCode.STOCK_NOT_FOUND));

        favoriteStockRepository.deleteByUserIdAndStockCode(userId, stockCode);
    }
}