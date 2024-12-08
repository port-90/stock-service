package com.port90.core.stockchart.application;

import com.port90.core.stockchart.dto.request.StockChartRequest;
import com.port90.core.stockchart.dto.response.ChartData;
import com.port90.core.stockchart.dto.response.StockChartResponse;
import com.port90.stockdomain.infrastructure.StockChartDailyRepository;
import com.port90.stockdomain.infrastructure.StockChartHourlyRepository;
import com.port90.stockdomain.infrastructure.StockChartMinuteRepository;
import com.port90.stockdomain.infrastructure.StockChartWeeklyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockChartService {

    private final StockChartMinuteRepository minuteRepository;
    private final StockChartHourlyRepository hourlyRepository;
    private final StockChartDailyRepository dailyRepository;
    private final StockChartWeeklyRepository weeklyRepository;

    public StockChartResponse getStockChart(StockChartRequest request) {
        List<ChartData> chartData = getChartData(request);

        return new StockChartResponse(request.stockCode(), request.chartType(), chartData);
    }

    private List<ChartData> getChartData(StockChartRequest request) {
        return switch (request.chartType()) {
            case MINUTE -> fetchMinuteData(request);
            case HOURLY -> fetchHourlyData(request);
            case DAILY -> fetchDailyData(request);
            case WEEKLY -> fetchWeeklyData(request);
            default -> throw new IllegalArgumentException();
        };
    }

    private List<ChartData> fetchMinuteData(StockChartRequest request) {
        return minuteRepository.findByStockCodeAndDateRange(
                        request.stockCode(),
                        request.startDate(),
                        request.endDate(),
                        request.startTime(),
                        request.endTime()
                )
                .stream()
                .map(entity -> new ChartData(
                        entity.getDate(),
                        entity.getTime(),
                        entity.getStartPrice(),
                        entity.getPrice(),
                        entity.getHighPrice(),
                        entity.getLowPrice(),
                        entity.getTradingVolume(),
                        entity.getTradingValue()
                ))
                .toList();
    }

    private List<ChartData> fetchHourlyData(StockChartRequest request) {
        return hourlyRepository.findByStockCodeAndDateRange(
                        request.stockCode(),
                        request.startDate(),
                        request.endDate(),
                        request.startTime(),
                        request.endTime()
                )
                .stream()
                .map(entity -> new ChartData(
                        entity.getDate(),
                        entity.getTime(),
                        entity.getOpenPrice(),
                        entity.getClosePrice(),
                        entity.getHighPrice(),
                        entity.getLowPrice(),
                        entity.getTradingVolume(),
                        entity.getTradingValue()
                ))
                .toList();
    }

    private List<ChartData> fetchDailyData(StockChartRequest request) {
        return dailyRepository.findByStockCodeAndDateRange(
                        request.stockCode(),
                        request.startDate(),
                        request.endDate()
                )
                .stream()
                .map(entity -> new ChartData(
                        entity.getDate(),
                        null,
                        entity.getOpenPrice(),
                        entity.getClosePrice(),
                        entity.getHighPrice(),
                        entity.getLowPrice(),
                        entity.getTotalVolume(),
                        entity.getTotalPrice()
                ))
                .toList();
    }

    private List<ChartData> fetchWeeklyData(StockChartRequest request) {
        return weeklyRepository.findByStockCodeAndDateRange(
                        request.stockCode(),
                        request.startDate(),
                        request.endDate()
                )
                .stream()
                .map(entity -> new ChartData(
                        entity.getDate(),
                        null,
                        entity.getOpenPrice(),
                        entity.getClosePrice(),
                        entity.getHighPrice(),
                        entity.getLowPrice(),
                        entity.getTotalVolume(),
                        entity.getTotalPrice()
                ))
                .toList();
    }
}
