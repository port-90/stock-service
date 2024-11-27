package com.port90.external.infrastructure;

import com.port90.external.domain.chart.StockChartDaily;
import com.port90.external.domain.chart.StockChartDailyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockChartDailyRepository extends JpaRepository<StockChartDaily, StockChartDailyId> {
}
