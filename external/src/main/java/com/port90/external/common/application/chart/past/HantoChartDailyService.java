package com.port90.external.common.application.chart.past;

import com.port90.external.common.client.HantoClient;
import com.port90.external.common.dto.HantoDailyChartResponse;
import com.port90.external.domain.HantoCredential;
import com.port90.stockdomain.domain.chart.StockChartDaily;
import com.port90.stockdomain.domain.chart.StockChartDailyId;
import com.port90.stockdomain.domain.info.StockInfo;
import com.port90.stockdomain.domain.info.StockInfoStatus;
import com.port90.stockdomain.infrastructure.StockChartDailyRepository;
import com.port90.stockdomain.infrastructure.StockInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HantoChartDailyService implements StockChartDailyService {
    private final StockInfoRepository stockInfoRepository;
    private final StockChartDailyRepository stockChartDailyRepository;
    private final StockChartDailyMapper stockChartDailyMapper;
    private final HantoClient hantoClient;

    @Override
    public void fetchAndSaveDailyStockData(LocalDate startDate, LocalDate endDate) {
        HantoCredential hantoCredential = hantoClient.getCredentials().getFirst();
        List<StockInfo> stockInfoList = stockInfoRepository.findByStatusNot(StockInfoStatus.CLOSE);
        int index = 0;
        for (StockInfo stockInfo : stockInfoList) {
            HantoDailyChartResponse dailyChartResponse = hantoClient.getDailyChart(hantoCredential, stockInfo.getStockCode(), startDate, endDate);
            List<StockChartDaily> stockChartDailies = stockChartDailyMapper.toEntityList(stockInfo.getStockCode(), dailyChartResponse);
            saveStockChartDailyList(stockChartDailies);
            index++;
            try {
                if (index % 15 == 0) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void fetchAndSaveDailyStockData(LocalDate date) {
        HantoCredential hantoCredential = hantoClient.getCredentials().getFirst();
        List<StockInfo> stockInfoList = stockInfoRepository.findByStatusNot(StockInfoStatus.CLOSE);
        LocalDate startDate = date.minusDays(1);
        int index = 0;
        for (StockInfo stockInfo : stockInfoList) {
            HantoDailyChartResponse dailyChartResponse = hantoClient.getDailyChart(hantoCredential, stockInfo.getStockCode(), startDate, date);
            List<StockChartDaily> stockChartDailies = stockChartDailyMapper.toEntityList(stockInfo.getStockCode(), dailyChartResponse);
            saveStockChartDailyList(stockChartDailies, date);
            index++;
            try {
                if (index % 15 == 0) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void saveStockChartDailyList(List<StockChartDaily> stockChartDailies, LocalDate date) {
        for (StockChartDaily stockChartDaily : stockChartDailies) {
            if (stockChartDaily.getDate().equals(date)) {
                StockChartDailyId stockChartDailyId = new StockChartDailyId(stockChartDaily);
                if (stockChartDailyRepository.existsById(stockChartDailyId)) {
                    stockChartDailyRepository.deleteById(stockChartDailyId);
                }
                stockChartDailyRepository.save(stockChartDaily);
            }
        }
    }

    @Transactional
    public void saveStockChartDailyList(List<StockChartDaily> stockChartDailies) {
        for (StockChartDaily stockChartDaily : stockChartDailies) {
            StockChartDailyId stockChartDailyId = new StockChartDailyId(stockChartDaily);
            if (stockChartDailyRepository.existsById(stockChartDailyId)) {
                stockChartDailyRepository.deleteById(stockChartDailyId);
            }
            stockChartDailyRepository.save(stockChartDaily);
        }
    }

}
