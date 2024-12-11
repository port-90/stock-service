package com.port90.aggregator.scheduler;

import com.port90.aggregator.application.HourlyAggregationService;
import com.port90.aggregator.application.MonthlyAggregationService;
import com.port90.aggregator.application.StockDataLoadService;
import com.port90.aggregator.application.WeeklyAggregationService;
import com.port90.stockdomain.domain.info.StockInfo;
import jakarta.transaction.Transactional;
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

    // 매일 10시부터 16시까지 매 시간 실행
    @Scheduled(cron = "0 0 10-16 * * *")
    @Transactional
    public void aggregateHourlyDataForAllStocks() {
        List<StockInfo> stockInfoList = stockDataLoadService.getNotClosedStockInfoList();
        LocalDate currentDate = LocalDate.now();
        LocalTime currentHour = LocalTime.now().truncatedTo(ChronoUnit.HOURS);

        log.info("currentHour: {}", currentHour);
        for (StockInfo stockInfo : stockInfoList) {
            try {
                hourlyAggregationService.aggregateHourlyData(
                        stockInfo.getStockCode(),
                        currentDate,
                        currentHour.minusHours(1),
                        currentHour
                );
                log.info("[Hourly Aggregation 성공] 주식코드: {}, 날짜: {}, 시간: {}~{}",
                        stockInfo.getStockCode(), currentDate, currentHour.minusHours(1), currentHour);
            } catch (Exception e) {
                log.error("[Hourly Aggregation 실패] 주식코드: {}, 날짜: {}, 시간: {}~{}. 에러: {}",
                        stockInfo.getStockCode(), currentDate, currentHour.minusHours(1), currentHour, e.getMessage(),
                        e);
            }
        }
    }

    // 매주 월요일 00:00 실행
    @Scheduled(cron = "0 0 0 * * MON")
    @Transactional
    public void aggregateWeeklyDataForAllStocks() {
        List<StockInfo> stockInfoList = stockDataLoadService.getNotClosedStockInfoList();
        LocalDate currentDate = LocalDate.now();

        for (StockInfo stockInfo : stockInfoList) {
            try {
                weeklyAggregationService.aggregateWeeklyData(
                        stockInfo.getStockCode(),
                        currentDate
                );
                log.info("[Weekly Aggregation 성공] 주식코드: {}, 기준 날짜: {}",
                        stockInfo.getStockCode(), currentDate);
            } catch (Exception e) {
                log.error("[Weekly Aggregation 실패] 주식코드: {}, 기준 날짜: {}. 에러: {}",
                        stockInfo.getStockCode(), currentDate, e.getMessage(), e);
            }
        }
    }

    // 매월 1일 00:00 실행
    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional
    public void aggregateMonthlyDataForAllStocks() {
        List<StockInfo> stockInfoList = stockDataLoadService.getNotClosedStockInfoList();
        YearMonth currentMonth = YearMonth.now();

        for (StockInfo stockInfo : stockInfoList) {
            try {
                monthlyAggregationService.aggregateMonthlyData(
                        stockInfo.getStockCode(),
                        currentMonth
                );
                log.info("[Monthly Aggregation 성공] 주식코드: {}, 기준 연월: {}",
                        stockInfo.getStockCode(), currentMonth);
            } catch (Exception e) {
                log.error("[Monthly Aggregation 실패] 주식코드: {}, 기준 연월: {}. 에러: {}",
                        stockInfo.getStockCode(), currentMonth, e.getMessage(), e);
            }
        }
    }
}
