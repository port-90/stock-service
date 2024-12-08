package com.port90.core.stockchart.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record StockChartRequest(
        @NotBlank String stockCode,
        @NotNull ChartType chartType,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull Integer limit,
        LocalTime startTime,
        LocalTime endTime
) {

}
