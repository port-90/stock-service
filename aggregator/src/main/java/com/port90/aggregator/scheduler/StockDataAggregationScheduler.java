package com.port90.aggregator.scheduler;

import com.port90.aggregator.application.HourlyAggregationService;
import com.port90.aggregator.application.MonthlyAggregationService;
import com.port90.aggregator.application.StockDataLoadService;
import com.port90.aggregator.application.WeeklyAggregationService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockDataAggregationScheduler {

    private final StockDataLoadService stockDataLoadService;
    private final HourlyAggregationService hourlyAggregationService;
    private final WeeklyAggregationService weeklyAggregationService;
    private final MonthlyAggregationService monthlyAggregationService;

    // 매일 10시부터 16시까지 매 시간 5분에 실행
    @Scheduled(cron = "0 5 10-16 * * *")
    public void aggregateHourlyDataForAllStocks() {
        List<String> stockCodeList = stockDataLoadService.getOpenedStockInfoList();
        LocalDate currentDate = LocalDate.now();
        LocalTime currentHour = LocalTime.now().truncatedTo(ChronoUnit.HOURS);

        log.info("currentHour: {}", currentHour);
        for (String stockCode : stockCodeList) {
            try {
                hourlyAggregationService.aggregateHourlyData(
                        stockCode,
                        currentDate,
                        currentHour.minusHours(1),
                        currentHour
                );
                log.info("[Hourly Aggregation 성공] 주식코드: {}, 날짜: {}, 시간: {}~{}",
                        stockCode, currentDate, currentHour.minusHours(1), currentHour);
            } catch (Exception e) {
                log.error("[Hourly Aggregation 실패] 주식코드: {}, 날짜: {}, 시간: {}~{}. 에러: {}",
                        stockCode, currentDate, currentHour.minusHours(1), currentHour, e.getMessage(),
                        e);
            }
        }
    }

    // 매주 금요일 20:00 실행
    @Scheduled(cron = "0 0 20 * * FRI")
    public void aggregateWeeklyDataForAllStocks() {
        List<String> stockCodeList = stockDataLoadService.getOpenedStockInfoList();
        LocalDate currentDate = LocalDate.now();

        for (String stockCode : stockCodeList) {
            try {
                weeklyAggregationService.aggregateWeeklyData(
                        stockCode,
                        currentDate
                );
                log.info("[Weekly Aggregation 성공] 주식코드: {}, 기준 날짜: {}",
                        stockCode, currentDate);
            } catch (Exception e) {
                log.error("[Weekly Aggregation 실패] 주식코드: {}, 기준 날짜: {}. 에러: {}",
                        stockCode, currentDate, e.getMessage(), e);
            }
        }
    }

    // 매월 1일 00:00 실행
    @Scheduled(cron = "0 0 0 1 * *")
    public void aggregateMonthlyDataForAllStocks() {
        List<String> stockCodeList = stockDataLoadService.getOpenedStockInfoList();
        YearMonth previousMonth = YearMonth.now().minusMonths(1);

        for (String stockCode : stockCodeList) {
            try {
                monthlyAggregationService.aggregateMonthlyData(
                        stockCode,
                        previousMonth
                );
                log.info("[Monthly Aggregation 성공] 주식코드: {}, 기준 연월: {}",
                        stockCode, previousMonth);
            } catch (Exception e) {
                log.error("[Monthly Aggregation 실패] 주식코드: {}, 기준 연월: {}. 에러: {}",
                        stockCode, previousMonth, e.getMessage(), e);
            }
        }
    }
}
