package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.chart.StockChartHourly;
import com.port90.stockdomain.domain.chart.StockChartHourlyId;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockChartHourlyRepository extends JpaRepository<StockChartHourly, StockChartHourlyId> {

    @Query("""
        SELECT h
        FROM StockChartHourly h
        WHERE h.stockCode = :stockCode
        AND (
            (h.date > :startDate AND h.date < :endDate) OR
            (h.date = :startDate AND h.time >= :startTime) OR
            (h.date = :endDate AND h.time <= :endTime)
        )
    """)
    List<StockChartHourly> findByStockCodeAndDateRange(
            @Param("stockCode") String stockCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
