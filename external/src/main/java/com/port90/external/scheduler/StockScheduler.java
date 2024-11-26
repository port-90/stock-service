package com.port90.external.scheduler;

import com.port90.external.service.HantoClient;
import com.port90.external.service.chart.current.StockChartMinuteService;
import com.port90.external.service.info.StockInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class StockScheduler {
    private final StockChartMinuteService stockChartMinuteService;
    private final StockInfoService stockInfoService;
    private final HantoClient hantoClient;


    @Scheduled(cron = "0 1 * * * ?")
    public void login() {
        hantoClient.login();
    }

    @Scheduled(cron = "0 2 * * * ?")
    public void syncStockInfo() {
        stockInfoService.fetchAndSaveAllStockInfoData();
    }

    // 매 분마다 실행
    @Scheduled(cron = "0 * * * * ?")
    public void syncMinuteChartData() throws InterruptedException {
        log.info("[START]");
        List<String> stockCodes = stockInfoService.getAllStockCodes();
        log.info("totalSize: {}", stockCodes.size());
        for (int i=0; i<stockCodes.size(); i++) {
            if (i%15==0) {
                Thread.sleep(1000);
            }
            String stockCode = stockCodes.get(i);
            stockChartMinuteService.fetchAndSaveMinuteStockData(stockCode);
        }
        log.info("[END]");
    }
}
