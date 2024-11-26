package com.port90.external.common.application.chart.past;

public interface StockChartDailyService {
    void fetchAndSaveDailyStockData(String stockCode);
}
