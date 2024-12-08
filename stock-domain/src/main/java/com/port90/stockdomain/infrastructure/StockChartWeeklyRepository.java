package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.chart.StockChartWeekly;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockChartWeeklyRepository extends JpaRepository<StockChartWeekly, Long> {

    @Query("""
                SELECT w
                FROM StockChartWeekly w
                WHERE w.stockCode = :stockCode
                AND :startDate <= w.date
                AND w.date <= :endDate
            """)
    List<StockChartWeekly> findByStockCodeAndDateRange(
            @Param("stockCode") String stockCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
