package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.chart.StockChartMonthly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockChartMonthlyRepository extends JpaRepository<StockChartMonthly, Long> {

}
