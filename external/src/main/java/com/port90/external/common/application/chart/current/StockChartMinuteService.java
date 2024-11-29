package com.port90.external.common.application.chart.current;

public interface StockChartMinuteService {
    void fetchAndSaveMinuteStockData(String stockCode);
}
