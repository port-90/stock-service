package com.port90.stockdomain.domain.chart;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StockChartMonthlyId implements Serializable {

    private String stockCode;
    private Integer year;
    private Integer month;
}
