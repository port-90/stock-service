package com.port90.external.service.chart.past;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.port90.external.entity.chart.StockChartDaily;
import com.port90.external.entity.chart.StockChartDailyId;
import com.port90.external.repository.StockChartDailyRepository;
import com.port90.external.service.NuriClient;
import com.port90.external.service.dto.NuriStockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NuriChartDailyService implements StockChartDailyService {

    private final StockChartDailyRepository stockChartDailyRepository;
    private final NuriClient nuriClient;
    private final ObjectMapper objectMapper;

    @Override
    public void fetchAndSaveDailyStockData(String stockCode) {
        List<NuriStockResponse> chartDailyResponses = nuriClient.getDailyStockData(stockCode, 30);
        convertToDomainAndSaveAllChartMinuteData(chartDailyResponses);
    }

    @Transactional
    public void convertToDomainAndSaveAllChartMinuteData(List<NuriStockResponse> stockResponses) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        for (NuriStockResponse stockResponse : stockResponses) {
            StockChartDaily stockChartDaily = new StockChartDaily();
            stockChartDaily.setStockCode(stockResponse.getSrtnCd());
            stockChartDaily.setDate(LocalDate.parse(stockResponse.getBasDt(), dateFormatter));
            stockChartDaily.setClosePrice(stockResponse.getClpr());
            stockChartDaily.setOpenPrice(stockResponse.getMkp());
            stockChartDaily.setHighPrice(stockResponse.getHipr());
            stockChartDaily.setLowPrice(stockResponse.getLopr());
            stockChartDaily.setTotalVolume(stockResponse.getTrqu());
            stockChartDaily.setTotalPrice(stockResponse.getTrPrc());

            StockChartDailyId stockChartMinuteId = new StockChartDailyId(stockChartDaily.getDate(), stockChartDaily.getStockCode());
            if (stockChartDailyRepository.existsById(stockChartMinuteId)) {
                break;
            } else {
                stockChartDailyRepository.save(stockChartDaily);
            }
        }
    }
}
