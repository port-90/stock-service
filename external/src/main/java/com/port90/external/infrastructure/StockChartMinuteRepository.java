package com.port90.external.infrastructure;

import com.port90.external.domain.chart.StockChartMinute;
import com.port90.external.domain.chart.StockChartMinuteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockChartMinuteRepository extends JpaRepository<StockChartMinute, StockChartMinuteId> {
}
