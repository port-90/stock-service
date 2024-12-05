package com.port90.external.common.application.chart.past;


import com.port90.external.common.client.NuriClient;
import com.port90.external.common.dto.NuriStockResponse;
import com.port90.stockdomain.domain.chart.StockChartDaily;
import com.port90.stockdomain.domain.chart.StockChartDailyId;
import com.port90.stockdomain.infrastructure.StockChartDailyRepository;
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

    @Override
    public void fetchAndSaveDailyStockData(String stockCode) {
        List<NuriStockResponse> chartDailyResponses = nuriClient.getDailyStockData(stockCode, 100);
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
