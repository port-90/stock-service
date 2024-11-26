package com.port90.external.repository;

import com.port90.external.entity.chart.StockChartMinute;
import com.port90.external.entity.chart.StockChartMinuteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockChartMinuteRepository extends JpaRepository<StockChartMinute, StockChartMinuteId> {
}
