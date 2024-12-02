package com.port90.external.scheduler;

import com.port90.external.common.client.HantoClient;
import com.port90.external.common.application.info.StockInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StockScheduler {
    private final StockInfoService stockInfoService;
    private final HantoClient hantoClient;


    @Scheduled(cron = "0 1 * * * ?")
    public void login() {
        hantoClient.loginAll();
    }

    @Scheduled(cron = "0 2 * * * ?")
    public void syncStockInfo() {
        stockInfoService.fetchAndSaveAllStockInfoData();
    }
}
