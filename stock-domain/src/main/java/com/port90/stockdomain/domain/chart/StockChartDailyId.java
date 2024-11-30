package com.port90.stockdomain.domain.chart;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StockChartDailyId implements Serializable {
    private LocalDate date;
    private String stockCode;
}
