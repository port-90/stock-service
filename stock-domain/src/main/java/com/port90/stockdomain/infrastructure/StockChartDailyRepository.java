package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.chart.StockChartDaily;
import com.port90.stockdomain.domain.chart.StockChartDailyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockChartDailyRepository extends JpaRepository<StockChartDaily, StockChartDailyId> {
}
