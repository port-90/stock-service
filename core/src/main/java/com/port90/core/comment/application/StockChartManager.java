package com.port90.core.comment.application;

import com.port90.stockdomain.domain.chart.StockChartMinute;
import com.port90.stockdomain.infrastructure.StockChartMinuteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockChartManager {
    private final StockChartMinuteRepository stockChartMinuteRepository;

    public StockChartMinute getTheMostRecentStockChartMinuteByStockCode(String stockCode) {
        return stockChartMinuteRepository
                .findFirstByStockCodeOrderByStockCodeAndDateAndTimeDesc(stockCode)
                .orElseThrow(() -> new RuntimeException("Stock Chart Minute Data Not Found"));
    }
}
