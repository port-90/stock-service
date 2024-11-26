package com.port90.external.service.chart.current;

public interface StockChartMinuteService {
    void fetchAndSaveMinuteStockData(String stockCode);
}
