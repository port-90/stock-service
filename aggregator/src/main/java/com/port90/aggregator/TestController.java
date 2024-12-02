package com.port90.aggregator;

import com.port90.aggregator.application.HourlyAggregationService;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final HourlyAggregationService hourlyAggregationService;

    @PostMapping
    public void testStockChartAggregation() {
        LocalDate specificDate = LocalDate.parse("2024-11-01");
        LocalTime startTime = LocalTime.parse("09:00:00");
        LocalTime endTime = LocalTime.parse("10:00:00");

        // 시간 측정 시작
        LocalDateTime startExecution = LocalDateTime.now();
        log.info("테스트 시작 시간: {}", startExecution);

        for (int stockIndex = 1; stockIndex <= 3000; stockIndex++) {
            String stockCode = String.format("%07d", stockIndex); // "0000001", "0000002", ..., "0030000"

            hourlyAggregationService.aggregateHourlyData(stockCode, specificDate, startTime, endTime);
        }

        // 전체 소요 시간 출력
        LocalDateTime endExecution = LocalDateTime.now();
        log.info("전체 소요 시간: {}ms",
                Duration.between(startExecution, endExecution).toMillis());
    }
}
