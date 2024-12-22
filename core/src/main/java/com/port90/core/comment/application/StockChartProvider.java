package com.port90.core.comment.application;

import com.port90.stockdomain.domain.chart.*;
import com.port90.stockdomain.infrastructure.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class StockChartProvider {
    private final StockChartMinuteRepository stockChartMinuteRepository;
    private final StockChartHourlyRepository stockChartHourlyRepository;
    private final StockChartDailyRepository stockChartDailyRepository;
    private final StockChartWeeklyRepository stockChartWeeklyRepository;
    private final StockChartMonthlyRepository stockChartMonthlyRepository;

    public StockChartMinute findStockChartMinuteCreatedAtBy(String stockCode, LocalDate date, LocalTime time) {
        return stockChartMinuteRepository.findById(new StockChartMinuteId(date, time, stockCode))
                .orElseThrow(() -> new RuntimeException("Stock Chart Minute Data Not Found"));
    }

    public StockChartHourly findStockChartHourlyCreatedAtBy(String stockCode, LocalDate date, LocalTime time) {
        return stockChartHourlyRepository.findById(new StockChartHourlyId(stockCode, date, time))
                .orElseThrow(() -> new RuntimeException("Stock Chart Hourly Data Not Found"));
    }

    public StockChartDaily findStockChartDailyCreatedAtBy(String stockCode, LocalDate date) {
        return stockChartDailyRepository.findById(new StockChartDailyId(date, stockCode))
                .orElseThrow(() -> new RuntimeException("Stock Chart Daily Data Not Found"));
    }

    public StockChartWeekly findStockChartWeeklyCreatedAtBy(String stockCode, LocalDate date) {
        return stockChartWeeklyRepository.findById(new StockChartWeeklyId(stockCode, date))
                .orElseThrow(() -> new RuntimeException("Stock Chart Weekly Data Not Found"));
    }

    public StockChartMonthly findStockChartMonthlyCreatedAtBy(String stockCode, Integer year, Integer month) {
        return stockChartMonthlyRepository.findById(new StockChartMonthlyId(stockCode, year, month))
                .orElseThrow(() -> new RuntimeException("Stock Chart Weekly Data Not Found"));
    }
}
