package com.port90.aggregator.application;

import com.port90.aggregator.exception.StockDataEmptyException;
import com.port90.aggregator.exception.StockErrorCode;
import com.port90.stockdomain.domain.chart.StockChartDaily;
import com.port90.stockdomain.domain.chart.StockChartMinute;
import com.port90.stockdomain.domain.info.StockInfo;
import com.port90.stockdomain.domain.info.StockInfoStatus;
import com.port90.stockdomain.infrastructure.StockChartDailyRepository;
import com.port90.stockdomain.infrastructure.StockChartMinuteRepository;
import com.port90.stockdomain.infrastructure.StockInfoRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockDataLoadService {

    private final StockChartMinuteRepository minuteRepository;
    private final StockChartDailyRepository dailyRepository;
    private final StockInfoRepository stockInfoRepository;

    public List<StockInfo> getNotClosedStockInfoList() {
        return stockInfoRepository.findByStatusNot(StockInfoStatus.CLOSE);
    }

    public List<StockChartMinute> getStockChartMinuteByStockCodeAndDateAndTimeBetween(String stockCode,
            LocalDate specificDate,
            LocalTime startTime,
            LocalTime endTime) {
        List<StockChartMinute> minuteData = minuteRepository.findByStockCodeAndDateAndTimeBetween(
                stockCode, specificDate, startTime, endTime);

        if (minuteData.isEmpty()) {
            throw new StockDataEmptyException(StockErrorCode.STOCK_DATA_EMPTY);
        }

        return minuteData;
    }

    public List<StockChartDaily> getStockChartDailyByStockCodeAndDateRange(String stockCode,
            LocalDate startOfMonth,
            LocalDate endOfMonth) {
        List<StockChartDaily> dailyData = dailyRepository.findByStockCodeAndDateRange(
                stockCode, startOfMonth, endOfMonth);

        if (dailyData.isEmpty()) {
            throw new StockDataEmptyException(StockErrorCode.STOCK_DATA_EMPTY);
        }

        return dailyData;
    }
}
