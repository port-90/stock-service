package com.port90.aggregator.application;

import com.port90.aggregator.dto.AggregatedResult;
import com.port90.stockdomain.domain.chart.StockChartHourly;
import com.port90.stockdomain.domain.chart.StockChartMinute;
import com.port90.stockdomain.infrastructure.StockChartHourlyRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HourlyAggregationService {

    private final StockDataLoadService stockDataLoadService;
    private final StockChartHourlyRepository hourlyRepository;
    private final AggregationCalculatorService aggregationCalculatorService;

    @Transactional
    public void aggregateHourlyData(String stockCode, LocalDate specificDate, LocalTime startTime, LocalTime endTime) {
        List<StockChartMinute> minuteData = stockDataLoadService.getStockChartMinuteByStockCodeAndDateAndTimeBetween(
                stockCode, specificDate, startTime, endTime);

        // 시가, 종가 계산
        String openPrice = minuteData.getFirst().getStartPrice();
        String closePrice = minuteData.getLast().getPrice();

        // 공통 데이터 계산 메서드 호출
        AggregatedResult result = aggregationCalculatorService.calculateAggregatedData(
                minuteData,
                StockChartMinute::getHighPrice,
                StockChartMinute::getLowPrice,
                StockChartMinute::getTradingVolume,
                StockChartMinute::getTradingValue
        );

        // Hourly 데이터 생성 및 저장
        StockChartHourly hourlyData = StockChartHourly.builder()
                .date(specificDate)
                .time(endTime)
                .stockCode(stockCode)
                .openPrice(openPrice)
                .closePrice(closePrice)
                .highPrice(result.highPrice())
                .lowPrice(result.lowPrice())
                .tradingValue(result.totalValue())
                .tradingVolume(result.totalVolume())
                .build();

        hourlyRepository.save(hourlyData);
    }
}
