package com.port90.external.common.application.chart.current;

import com.port90.external.common.client.HantoClient;
import com.port90.external.common.dto.StockResponse;
import com.port90.external.domain.HantoCredential;
import com.port90.stockdomain.domain.chart.StockChartMinute;
import com.port90.stockdomain.domain.chart.StockChartMinuteId;
import com.port90.stockdomain.infrastructure.StockChartMinuteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockChartTask {
    private final StockChartMinuteRepository stockChartMinuteRepository;
    private final HantoClient hantoClient;

    @Async
    public void processStocks(HantoCredential credentials, List<String> stockCodes) {
        LocalTime baseTime = LocalTime.now();
        for (String stockCode : stockCodes) {
            List<StockResponse> responses = hantoClient.getDailyMinute(credentials, stockCode, baseTime);
            log.info("[{}] {}", hantoClient, stockCode);
            convertToDomainAndSaveAllChartMinuteData(responses);
        }
    }


    public void convertToDomainAndSaveAllChartMinuteData(List<StockResponse> stockResponses) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        for (StockResponse stockResponse : stockResponses) {
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

            StockChartMinuteId stockChartMinuteId = new StockChartMinuteId(stockChartMinute.getDate(), stockChartMinute.getTime(), stockChartMinute.getStockCode());
            if (stockChartMinuteRepository.existsById(stockChartMinuteId)) {
                break;
            } else {
                stockChartMinuteRepository.save(stockChartMinute);
            }
        }
    }
}
