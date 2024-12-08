package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.chart.StockChartMinute;
import com.port90.stockdomain.domain.chart.StockChartMinuteId;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockChartMinuteRepository extends JpaRepository<StockChartMinute, StockChartMinuteId> {

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

    StockChartMinute findFirstByStockCodeEqualsAndDateEqualsAndTimeBefore(String stockCode, LocalDate date,
            LocalTime time);


    @Query("""
            SELECT m
            FROM StockChartMinute m
            WHERE m.stockCode = :stockCode
            AND (
                (m.date > :startDate AND m.date < :endDate) OR
                (m.date = :startDate AND m.time >= :startTime) OR
                (m.date = :endDate AND m.time <= :endTime)
            )
            """)
    List<StockChartMinute> findByStockCodeAndDateRange(
            @Param("stockCode") String stockCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
