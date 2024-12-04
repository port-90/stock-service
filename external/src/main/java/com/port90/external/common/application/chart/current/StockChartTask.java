package com.port90.external.common.application.chart.current;

import com.port90.external.common.client.HantoClient;
import com.port90.external.common.dto.StockResponse;
import com.port90.external.domain.HantoCredential;
import com.port90.stockdomain.domain.chart.StockChartMinute;
import com.port90.stockdomain.infrastructure.StockChartMinuteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockChartTask {
    private final StockChartMinuteRepository stockChartMinuteRepository;
    private final StockChartMinuteMapper stockChartMinuteMapper;
    private final HantoClient hantoClient;

    @Async
    public void processStocks(HantoCredential credentials, List<String> stockCodes) {
        LocalTime baseTime = LocalTime.now();
        for (String stockCode : stockCodes) {
            List<StockResponse> responses = hantoClient.getDailyMinute(credentials, stockCode, baseTime);
            log.info("[API CALLED] {}", stockCode);
            convertToDomainAndSaveAllChartMinuteData(responses);
        }
    }

    public void convertToDomainAndSaveAllChartMinuteData(List<StockResponse> stockResponses) {
        StockChartMinute recentSavedStock = new StockChartMinute();

        for (int i = 0; i < stockResponses.size(); i++) {
            StockChartMinute stockChartMinute = stockChartMinuteMapper.toEntity(stockResponses.get(i));
            if (i == 0) {
                recentSavedStock = stockChartMinute;
            }

            // 저장되있는 가장 최근 분봉이 지금 저장하려는것보다 나중 분봉인경우 빠져나온다.
            if (recentSavedStock.getTime().isAfter(stockChartMinute.getTime())) {
                break;
            }

            stockChartMinuteRepository.save(stockChartMinute);
        }
    }
}
