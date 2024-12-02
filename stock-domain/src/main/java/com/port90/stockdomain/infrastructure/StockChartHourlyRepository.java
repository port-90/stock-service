package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.chart.StockChartHourly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockChartHourlyRepository extends JpaRepository<StockChartHourly, Long> {

}
