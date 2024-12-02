package com.port90.stockdomain.domain.chart;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StockChartHourlyId implements Serializable {

    private String stockCode;
    private LocalDate date;
    private LocalTime time;
}
