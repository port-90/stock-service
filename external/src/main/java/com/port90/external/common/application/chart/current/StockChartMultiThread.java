package com.port90.external.common.application.chart.current;

import com.port90.external.common.client.HantoClient;
import com.port90.external.domain.HantoCredential;
import com.port90.stockdomain.infrastructure.StockInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor
@Component
public class StockChartMultiThread {
    private final StockInfoRepository stockInfoRepository;
    private final StockChartTask stockChartTask;
    private final HantoClient hantoClient;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4); // 4개의 스레드 생성

    @Scheduled(cron = "0 * 9-15 * * 1-5")
    public void run() {
        List<HantoCredential> credentials = hantoClient.getCredentials();
        String result = hantoClient.isHoliday(credentials.getFirst(), LocalDate.now());
        if (result.equals("N")) return;

        List<String> stockCodes = stockInfoRepository.findAllStockCodes();
        List<List<String>> stockCodeGroups = groupStockCodes(stockCodes);
        // 작업 분배 및 실행
        for (int i = 0; i < credentials.size(); i++) {
            HantoCredential credential = credentials.get(i);
            List<String> stockCodeGroup = stockCodeGroups.get(i);
            // 각 credential 별 작업을 비동기로 실행
            executorService.submit(() -> stockChartTask.processStocks(credential, stockCodeGroup));
        }
    }


    private List<List<String>> groupStockCodes(List<String> stockCodes) {
        List<List<String>> result = new ArrayList<>();
        int capacity = stockCodes.size()/4;
        for (int i = 0; i < stockCodes.size(); i += capacity) {
            int end = Math.min(i + capacity, stockCodes.size());
            result.add(new ArrayList<>(stockCodes.subList(i, end)));
        }

        return result;
    }
}
