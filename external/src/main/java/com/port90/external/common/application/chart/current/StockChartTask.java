package com.port90.external.common.application.chart.current;

import com.port90.external.common.client.HantoClient;
import com.port90.external.common.dto.StockResponse;
import com.port90.external.domain.HantoCredential;
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
    private final HantoClient hantoClient;
    private final StockChartMinuteSaveService stockChartMinuteSaveService;

    @Async
    public void processStocks(HantoCredential credentials, List<String> stockCodes) {
        LocalTime startTime = LocalTime.now();
        while (startTime.isBefore(LocalTime.of(15, 31))) {
            LocalTime baseTime = LocalTime.now().minusMinutes(1);  // 현재 시간으로 기본 시간 설정
            log.info("[CREDENTIALS] {}", credentials.getName());

            int i = 0;
            long start = System.currentTimeMillis();
            for (String stockCode : stockCodes) {
                List<StockResponse> responses = hantoClient.getMinuteChart(credentials, stockCode, baseTime);
                stockChartMinuteSaveService.convertToDomainAndSaveAllChartMinuteData(responses);
                i++;
                if (i % 15 == 0) {
                    long end = System.currentTimeMillis();
                    long delay = 1000 - (start - end);
                    log.info("[TIME CALC] {}, {}, {}", start, end, delay);
                    if (delay > 0) {
                        try {
                            Thread.sleep(delay);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    start = System.currentTimeMillis();
                }
            }

            while (LocalTime.now().getMinute() == startTime.getMinute()) {
                // 분이 안바뀌었으면 1초대기
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            startTime = LocalTime.now();
        }
    }

}
