package com.port90.external.common.application.chart.current;

import com.port90.external.common.dto.StockResponse;
import com.port90.stockdomain.domain.chart.StockChartMinute;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class StockChartMinuteMapper {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

    public StockChartMinute toEntity(StockResponse stockResponse) {
        StockChartMinute stockChartMinute = new StockChartMinute();
        stockChartMinute.setStockCode(stockResponse.getStockCode());
        stockChartMinute.setDate(LocalDate.parse(stockResponse.getDate(), dateFormatter));
        stockChartMinute.setTime(LocalTime.parse(stockResponse.getTime(), timeFormatter));
        stockChartMinute.setPrice(stockResponse.getPrice()); // 현재가
        stockChartMinute.setStartPrice(stockResponse.getStartPrice()); // 시가
        stockChartMinute.setHighPrice(stockResponse.getHighPrice()); // 고가
        stockChartMinute.setLowPrice(stockResponse.getLowPrice()); // 저가
        stockChartMinute.setTradingVolume(stockResponse.getTradingVolume());
        stockChartMinute.setTradingValue(stockResponse.getTradingValue());
        return stockChartMinute;
    }
}
