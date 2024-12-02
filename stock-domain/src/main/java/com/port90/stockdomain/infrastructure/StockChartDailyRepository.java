package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.chart.StockChartDaily;
import com.port90.stockdomain.domain.chart.StockChartDailyId;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockChartDailyRepository extends JpaRepository<StockChartDaily, StockChartDailyId> {

    @Query("""
            SELECT d FROM StockChartDaily d
            WHERE d.stockCode = :stockCode
            AND :startDate <= d.date
            AND d.date <= :endDate
            ORDER BY d.date ASC
            """)
    List<StockChartDaily> findByStockCodeAndDateRange(
            String stockCode,
            LocalDate startDate,
            LocalDate endDate
    );
}
