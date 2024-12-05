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


    @Scheduled(cron = "0 0 1 * * *")
    public void login() {
        List<HantoCredential> hantoCredentials = hantoClient.loginAll();
        hantoClient.isHoliday(hantoCredentials.getFirst(), LocalDate.now());
        hantoClient.isHoliday(hantoCredentials.getFirst(), LocalDate.now().minusDays(1));
    }

    //@Scheduled(cron = "0 2 * * * ?")
//    public void syncStockInfoAndDailyChart() {
//        List<NuriStockResponse> stockResponses = stockInfoService.fetchStockData();
//        stockInfoService.saveTodayStockInfo(stockResponses); // 어제의 종목정보에서 오늘 이벤트 있는 종목 정보를 빼야함.
//        stockChartDailyService.saveYesterDayChart(stockResponses);
        // 1. 신규 상장종목이 서비스 되야함. 거래 정지된 종목들 알수 있어야함.
        // 상장종목 가져와서 서비스.....
    //}
}
