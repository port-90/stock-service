package com.port90.external.common.application.chart.current;

import com.port90.stockdomain.domain.chart.StockChartMinute;
import com.port90.stockdomain.infrastructure.StockChartMinuteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@SpringBootTest
class StockChartTaskTest {

    @Autowired
    StockChartMinuteRepository stockChartMinuteRepository;
    @Test
    void timeTest() {
        String stockCode = "005670";
        StockChartMinute recent = stockChartMinuteRepository.findFirstByStockCodeEqualsAndDateEqualsAndTimeBefore(stockCode,LocalDate.now(), LocalTime.now());
        System.out.println(recent);
    }
}