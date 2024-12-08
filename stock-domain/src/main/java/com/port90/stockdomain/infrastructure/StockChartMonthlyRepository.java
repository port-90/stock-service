package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.chart.StockChartMonthly;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockChartMonthlyRepository extends JpaRepository<StockChartMonthly, Long> {

    @Query("""
                SELECT m
                FROM StockChartMonthly m
                WHERE m.stockCode = :stockCode
                AND (m.year > :startYear OR (m.year = :startYear AND m.month >= :startMonth))
                AND (m.year < :endYear OR (m.year = :endYear AND m.month <= :endMonth))
            """)
    List<StockChartMonthly> findByStockCodeAndDateRange(
            @Param("stockCode") String stockCode,
            @Param("startYear") Integer startYear,
            @Param("startMonth") Integer startMonth,
            @Param("endYear") Integer endYear,
            @Param("endMonth") Integer endMonth);
}
