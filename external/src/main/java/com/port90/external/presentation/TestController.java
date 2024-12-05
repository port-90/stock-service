package com.port90.external.presentation;

import com.port90.external.common.application.chart.current.StockChartMultiThread;
import com.port90.external.common.application.chart.past.StockChartDailyService;
import com.port90.external.common.application.info.StockInfoService;
import com.port90.external.common.client.HantoClient;
import com.port90.external.common.dto.StockResponse;
import com.port90.external.domain.HantoCredential;
import com.port90.external.repository.HantoCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stock/admin")
public class TestController {
    private final StockChartDailyService stockChartDailyService;
    private final StockInfoService stockInfoService;
    private final HantoClient hantoClient;
    private final StockChartMultiThread stockChartMultiThread;
    private final HantoCredentialRepository credentialRepository;

    @GetMapping("/fetchNsave/chart/daily")
    public void fetchDailyStock() {
        stockChartDailyService.fetchAndSaveDailyStockData("005930");
    }

    @GetMapping("/fetchNsave/chart/minute")
    public void fetchMinuteStock() {
        stockChartMultiThread.run();
    }

    @GetMapping("/fetch/chart/minute/{stockCode}")
    public List<StockResponse> getStockChartMinuteOne(@PathVariable String stockCode) {
        List<HantoCredential> hantoCredentials = credentialRepository.findAll();
        return hantoClient.getDailyMinute(hantoCredentials.getFirst(), stockCode, LocalTime.now());
    }

    @GetMapping("/login/all")
    public void loginAll() {
        List<HantoCredential> hantoCredentials = hantoClient.loginAll();
        hantoClient.isHoliday(hantoCredentials.getFirst(), LocalDate.now());
    }

    @GetMapping("/login/{name}")
    public String login(@PathVariable String name) {
        HantoCredential hantoCredential = hantoClient.login(name);
        return hantoCredential.getAccessToken();
    }

    @GetMapping("/holiday/{date}")
    public String isHoliday(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date) {
        List<HantoCredential> hantoCredentials = credentialRepository.findAll();
        return hantoClient.isHoliday(hantoCredentials.getFirst(), date);
    }

    @GetMapping("/stock-info")
    public void saveStockInfo() {
        stockInfoService.fetchStockInfo();
    }

}