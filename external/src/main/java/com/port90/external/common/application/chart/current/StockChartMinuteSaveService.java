package com.port90.external.common.application.chart.current;

import com.port90.external.common.dto.StockResponse;
import com.port90.stockdomain.domain.chart.StockChartMinute;
import com.port90.stockdomain.infrastructure.StockChartMinuteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockChartMinuteSaveService {
    private final StockChartMinuteRepository stockChartMinuteRepository;
    private final StockChartMinuteMapper stockChartMinuteMapper;

    @Transactional
    public void convertToDomainAndSaveAllChartMinuteData(List<StockResponse> stockResponses) {

        StockChartMinute recentSavedStock = new StockChartMinute();
        for (int i = 0; i < stockResponses.size(); i++) {
            StockChartMinute stockChartMinute = stockChartMinuteMapper.toEntity(stockResponses.get(i));
            if (i == 0) {
                recentSavedStock = stockChartMinuteRepository.findFirstByStockCodeEqualsAndDateEqualsAndTimeBefore(
                        stockChartMinute.getStockCode(),
                        stockChartMinute.getDate(),
                        stockChartMinute.getTime()
                );
                log.debug("[RECENT] {}", recentSavedStock);
                if (Objects.isNull(recentSavedStock)) {
                    recentSavedStock = stockChartMinute;
                    stockChartMinuteRepository.save(stockChartMinute);
                }
            }

            // 저장되있는 가장 최근 분봉이 지금 저장하려는것보다 나중 분봉인경우 빠져나온다.
            log.debug("[COMPARE] recent: {}, current: {}", recentSavedStock, stockChartMinute);
            if (!recentSavedStock.getTime().isBefore(stockChartMinute.getTime())) {
                log.debug("already saved data");
                break;
            }

            stockChartMinuteRepository.save(stockChartMinute);
            log.debug("[StockChartMinute] saved : {}", stockChartMinute);
        }
    }
}
