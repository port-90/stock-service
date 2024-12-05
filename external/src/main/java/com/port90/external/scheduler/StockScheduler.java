package com.port90.external.scheduler;

import com.port90.external.common.client.HantoClient;
import com.port90.external.common.application.info.StockInfoService;
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
    private final HantoClient hantoClient;


    @Scheduled(cron = "0 0 1 * * *")
    public void login() {
        List<HantoCredential> hantoCredentials = hantoClient.loginAll();
        hantoClient.isHoliday(hantoCredentials.getFirst(), LocalDate.now());
    }

    //@Scheduled(cron = "0 2 * * * ?")
    public void syncStockInfo() {
        stockInfoService.fetchAndSaveAllStockInfoData();
    }
}
