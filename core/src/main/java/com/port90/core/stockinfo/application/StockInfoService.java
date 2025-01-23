package com.port90.core.stockinfo.application;

import com.port90.core.stockinfo.domain.StockInfoDto;
import com.port90.core.stockinfo.infrastructure.impl.StockInfoQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockInfoService {

    private final StockInfoQueryRepository stockInfoQueryRepository;

    public List<StockInfoDto> getStockInfoListByCondition(
            String stockCode, String stockName, String cursor, int size
    ) {
        return stockInfoQueryRepository
                .findAllByCondition(stockCode, stockName, cursor, size)
                .stream()
                .map(StockInfoDto::from)
                .toList();
    }
}
