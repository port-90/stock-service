package com.port90.aggregator.application;

import com.port90.aggregator.dto.AggregatedResult;
import com.port90.stockdomain.domain.chart.StockChartDaily;
import com.port90.stockdomain.domain.chart.StockChartWeekly;
import com.port90.stockdomain.infrastructure.StockChartWeeklyRepository;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeeklyAggregationService {

    private final StockDataLoadService stockDataLoadService;
    private final StockChartWeeklyRepository weeklyRepository;
    private final AggregationCalculatorService aggregationCalculatorService;

    @Transactional
    public void aggregateWeeklyData(String stockCode, LocalDate dateInWeek) {
        LocalDate startOfWeek = dateInWeek.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = dateInWeek.with(DayOfWeek.SUNDAY);

        // 월봉 데이터 계산에 필요한 일봉 데이터 조회
        List<StockChartDaily> dailyData = stockDataLoadService.getStockChartDailyByStockCodeAndDateRange(
                stockCode, startOfWeek, endOfWeek);

        // 공통 데이터 계산 메서드 호출
        AggregatedResult result = aggregationCalculatorService.calculateAggregatedData(
                dailyData,
                StockChartDaily::getHighPrice,
                StockChartDaily::getLowPrice,
                StockChartDaily::getTotalVolume,
                StockChartDaily::getTotalPrice
        );

        // 시가, 종가 계산
        String openPrice = dailyData.getFirst().getOpenPrice();
        String closePrice = dailyData.getLast().getClosePrice();

        // Weekly 데이터 생성 및 저장
        StockChartWeekly weeklyData = StockChartWeekly.builder()
                .stockCode(stockCode)
                .date(endOfWeek)
                .openPrice(openPrice)
                .closePrice(closePrice)
                .highPrice(result.highPrice())
                .lowPrice(result.lowPrice())
                .totalVolume(result.totalVolume())
                .totalPrice(result.totalValue())
                .build();

        weeklyRepository.save(weeklyData);
    }

}
