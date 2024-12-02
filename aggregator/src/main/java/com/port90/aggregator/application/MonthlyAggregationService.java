package com.port90.aggregator.application;

import com.port90.aggregator.dto.AggregatedResult;
import com.port90.stockdomain.domain.chart.StockChartDaily;
import com.port90.stockdomain.domain.chart.StockChartMonthly;
import com.port90.stockdomain.infrastructure.StockChartMonthlyRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonthlyAggregationService {


    private final StockDataLoadService stockDataLoadService;
    private final StockChartMonthlyRepository monthlyRepository;
    private final AggregationCalculatorService aggregationCalculatorService;


    @Transactional
    public void aggregateMonthlyData(String stockCode, YearMonth yearMonth) {
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        // 월봉 데이터 계산에 필요한 일봉 데이터 조회
        List<StockChartDaily> dailyData = stockDataLoadService.getStockChartDailyByStockCodeAndDateRange(
                stockCode, startOfMonth, endOfMonth);

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

        // Monthly 데이터 생성 및 저장
        StockChartMonthly monthlyData = StockChartMonthly.builder()
                .stockCode(stockCode)
                .year(yearMonth.getYear())
                .month(yearMonth.getMonthValue())
                .openPrice(openPrice)
                .closePrice(closePrice)
                .highPrice(result.highPrice())
                .lowPrice(result.lowPrice())
                .totalVolume(result.totalVolume())
                .totalPrice(result.totalValue())
                .build();

        monthlyRepository.save(monthlyData);
    }

}
