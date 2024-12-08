package com.port90.core.stockchart.dto.response;

import com.port90.core.stockchart.dto.request.ChartType;
import java.util.List;

public record StockChartResponse(
        String stockCode,
        ChartType chartType,
        List<ChartData> data
) {

}
