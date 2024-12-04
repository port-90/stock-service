package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.chart.StockChartMinute;
import com.port90.stockdomain.domain.chart.StockChartMinuteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface StockChartMinuteRepository extends JpaRepository<StockChartMinute, StockChartMinuteId> {
    StockChartMinute findFirstByStockCodeEqualsAndDateEqualsAndTimeBefore(String stockCode, LocalDate date, LocalTime time);
}
