package com.port90.external.presentation;

import com.port90.external.common.application.chart.current.StockChartMultiThread;
import com.port90.external.common.client.HantoClient;
import com.port90.external.common.application.chart.past.StockChartDailyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final StockChartDailyService stockChartDailyService;

    private final HantoClient hantoClient;
    private final StockChartMultiThread stockChartMultiThread;

    @GetMapping("/fetchNsave/chart/daily")
    public void test2() {
        stockChartDailyService.fetchAndSaveDailyStockData("005930");
    }

    @GetMapping("/fetchNsave/chart/minute")
    public void test5() {
        stockChartMultiThread.run();
    }

    @GetMapping("/login")
    public void test4() {
        hantoClient.loginAll();
    }
}
