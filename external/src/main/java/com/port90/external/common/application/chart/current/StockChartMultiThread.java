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

@Slf4j
@RequiredArgsConstructor
@Component
public class StockChartMultiThread {
    public static final int API_CAPACITY = 15;
    private final StockInfoRepository stockInfoRepository;
    private final StockChartTask stockChartTask;
    private final HantoClient hantoClient;

    @Scheduled(cron = "0 * 9-15 * * 1-5")
    public void run() {
        List<HantoCredential> credentials = hantoClient.getCredentials();
        String result = hantoClient.isHoliday(credentials.getFirst(), LocalDate.now());
        if (result.equals("N")) return;

        List<String> stockCodes = stockInfoRepository.findAllStockCodes();
        List<List<String>> stockCodeGroups = groupStockCodes(stockCodes);
        try {

            for (int i = 0; i < stockCodeGroups.size(); i += 4) {
                stockChartTask.processStocks(credentials.get(0), stockCodeGroups.get(i));
                if (i + 1 < stockCodeGroups.size()) {
                    stockChartTask.processStocks(credentials.get(1), stockCodeGroups.get(i + 1));
                }
                if (i + 2 < stockCodeGroups.size()) {
                    stockChartTask.processStocks(credentials.get(2), stockCodeGroups.get(i + 2));
                }
                if (i + 3 < stockCodeGroups.size()) {
                    stockChartTask.processStocks(credentials.get(3), stockCodeGroups.get(i + 3));
                }
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<List<String>> groupStockCodes(List<String> stockCodes) {
        List<List<String>> result = new ArrayList<>();

        for (int i = 0; i < stockCodes.size(); i += API_CAPACITY) {
            int end = Math.min(i + API_CAPACITY, stockCodes.size());
            result.add(new ArrayList<>(stockCodes.subList(i, end)));
        }

        return result;
    }
}
