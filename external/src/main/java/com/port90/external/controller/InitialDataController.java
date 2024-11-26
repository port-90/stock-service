package com.port90.external.controller;

import com.port90.external.service.HantoClient;
import com.port90.external.service.chart.current.HantoChartMinuteService;
import com.port90.external.service.info.NuriStockInfoService;
import com.port90.external.service.chart.past.StockChartDailyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class InitialDataController {
    private final StockChartDailyService stockChartDailyService;
    private final HantoChartMinuteService hantoChartMinuteService;
    private final NuriStockInfoService nuriStockInfoService;
    private final HantoClient hantoClient;
    @GetMapping("/test")
    public void test() {
        hantoChartMinuteService.fetchAndSaveMinuteStockData("005930");
        hantoChartMinuteService.fetchAndSaveMinuteStockData("394800");
        hantoChartMinuteService.fetchAndSaveMinuteStockData("294090");
        hantoChartMinuteService.fetchAndSaveMinuteStockData("035420");
        hantoChartMinuteService.fetchAndSaveMinuteStockData("426330");
    }

    @GetMapping("/test2")
    public void test2() {
        stockChartDailyService.fetchAndSaveDailyStockData("005930");
    }

    @GetMapping("/test3")
    public void test3() {
        nuriStockInfoService.fetchAndSaveAllStockInfoData();
    }

    @GetMapping("/test4")
    public void test4() {
        hantoClient.login();
    }
}
