package com.port90.external.presentation;

import com.port90.external.common.application.info.StockInfoKafkaProducer;
import com.port90.external.common.client.HantoClient;
import com.port90.external.common.application.chart.current.HantoChartMinuteService;
import com.port90.external.common.application.info.NuriStockInfoService;
import com.port90.external.common.application.chart.past.StockChartDailyService;
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
    private final StockInfoKafkaProducer stockInfoKafkaProducer;
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

    @GetMapping("/save/stock-info")
    public void test3() {
        nuriStockInfoService.fetchAndSaveAllStockInfoData();
    }

    @GetMapping("/send/stock-info")
    public void test3_1() {
        nuriStockInfoService.sendStockCodesToKafkaFromDB();
    }

    @GetMapping("/test4")
    public void test4() {
        hantoClient.login();
    }

    @GetMapping("/test5")
    public void test5() {
        //stockInfoKafkaProducer.sendStockCode("11111");
    }
}
