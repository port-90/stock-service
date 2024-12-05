package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.chart.StockChartMinute;
import com.port90.stockdomain.domain.chart.StockChartMinuteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface StockChartMinuteRepository extends JpaRepository<StockChartMinute, StockChartMinuteId> {

    // TODO: 분봉 데이터 정확한 계산 방법 확인 필요
    @Query("""
            SELECT m FROM StockChartMinute m
            WHERE m.stockCode = :stockCode
            AND m.date = :date
            AND :startTime < m.time
            AND m.time <= :endTime
            ORDER BY m.time ASC
            """)
    List<StockChartMinute> findByStockCodeAndDateAndTimeBetween(
            @Param("stockCode") String stockCode,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
    StockChartMinute findFirstByStockCodeEqualsAndDateEqualsAndTimeBefore(String stockCode, LocalDate date, LocalTime time);
}
