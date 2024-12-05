package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.chart.StockChartWeekly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockChartWeeklyRepository extends JpaRepository<StockChartWeekly, Long> {

}
