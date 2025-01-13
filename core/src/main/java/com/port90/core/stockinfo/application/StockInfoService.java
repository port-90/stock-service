package com.port90.core.stockinfo.application;

import com.port90.core.stockinfo.dto.StockInfoDto;
import com.port90.stockdomain.infrastructure.StockInfoQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockInfoService {

    private final StockInfoQueryRepository stockInfoQueryRepository;

    public List<StockInfoDto> getStockInfosByCondition(
            String stockCode, String stockName, String cursor, int size
    ) {
        return stockInfoQueryRepository
                .findStockInfosByCondition(stockCode, stockName, cursor, size)
                .stream().map(StockInfoDto::from).toList();
    }
}
