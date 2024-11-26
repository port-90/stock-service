package com.port90.external.repository;

import com.port90.external.entity.chart.StockChartDaily;
import com.port90.external.entity.chart.StockChartDailyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockChartDailyRepository extends JpaRepository<StockChartDaily, StockChartDailyId> {
}
