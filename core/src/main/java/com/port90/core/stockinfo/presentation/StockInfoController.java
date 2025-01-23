package com.port90.core.stockinfo.presentation;

import com.port90.core.stockinfo.application.StockInfoService;
import com.port90.core.stockinfo.presentation.response.StockInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock-infos")
public class StockInfoController {

    private final StockInfoService stockInfoService;

    @GetMapping
    public List<StockInfoResponse> getStockInfoListByCondition(
            @RequestParam(required = false) String stockCode,
            @RequestParam(required = false) String stockName,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return StockInfoResponse.from(
                stockInfoService.getStockInfoListByCondition(stockCode, stockName, cursor, size)
        );
    }
}
