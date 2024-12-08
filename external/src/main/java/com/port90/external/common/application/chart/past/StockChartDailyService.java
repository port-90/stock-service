package com.port90.external.common.application.chart.past;

import java.time.LocalDate;

public interface StockChartDailyService {
    void fetchAndSaveDailyStockData(LocalDate date);
    void fetchAndSaveDailyStockData(LocalDate startDate, LocalDate endDate);
}
