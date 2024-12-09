package com.port90.external.scheduler;

import com.port90.external.common.application.chart.past.StockChartDailyService;
import com.port90.external.common.application.info.StockInfoService;
import com.port90.external.common.client.HantoClient;
import com.port90.external.domain.HantoCredential;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class StockScheduler {
    private final StockInfoService stockInfoService;
    private final StockChartDailyService stockChartDailyService;
    private final HantoClient hantoClient;


    @Scheduled(cron = "0 0 3 * * *")
    public void login() {
        List<HantoCredential> hantoCredentials = hantoClient.loginAll();
        hantoClient.isHoliday(hantoCredentials.getFirst(), LocalDate.now());
        hantoClient.isHoliday(hantoCredentials.getFirst(), LocalDate.now().minusDays(1));
    }

    @Scheduled(cron = "0 0 7 * * * ")
    public void syncStockInfo() {
        stockInfoService.updateStockInfoWithDetail();
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void syncDailyChart() {
        stockChartDailyService.fetchAndSaveDailyStockData(LocalDate.now());
    }

}
