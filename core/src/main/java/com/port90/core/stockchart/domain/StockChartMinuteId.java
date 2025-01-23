package com.port90.core.stockchart.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public record StockChartMinuteId(
        String stockCode,
        LocalDate date,
        LocalTime time
) {
    public static StockChartMinuteId of(String stockCode, LocalDate date, LocalTime time) {
        return new StockChartMinuteId(stockCode, date, time);
    }
}
