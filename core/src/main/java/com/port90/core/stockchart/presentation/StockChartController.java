package com.port90.core.stockchart.presentation;

import com.port90.core.stockchart.application.StockChartService;
import com.port90.core.stockchart.dto.request.StockChartRequest;
import com.port90.core.stockchart.dto.response.StockChartResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockChartController {

    private final StockChartService stockChartService;

    @GetMapping("/stock-charts")
    public StockChartResponse getStockChart(@Valid StockChartRequest request) {
        return stockChartService.getStockChart(request);
    }
}
