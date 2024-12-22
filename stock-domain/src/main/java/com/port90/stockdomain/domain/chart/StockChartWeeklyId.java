package com.port90.stockdomain.domain.chart;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StockChartWeeklyId {

    private String stockCode;
    private LocalDate date;
}
