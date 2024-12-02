package com.port90.stockdomain.domain.chart;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_chart_monthly")
@Getter
@IdClass(StockChartMonthlyId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockChartMonthly {

    @Id
    @Column(nullable = false)
    private String stockCode;
    @Id
    @Column(nullable = false)
    private Integer year;
    @Id
    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private String openPrice;
    @Column(nullable = false)
    private String closePrice;
    @Column(nullable = false)
    private String highPrice;
    @Column(nullable = false)
    private String lowPrice;
    @Column(nullable = false)
    private String totalVolume;
    @Column(nullable = false)
    private String totalPrice;

    @Builder
    @SuppressWarnings({"unused", "java:S107"})
    public StockChartMonthly(String stockCode,
            Integer year,
            Integer month,
            String openPrice,
            String closePrice,
            String highPrice,
            String lowPrice,
            String totalVolume,
            String totalPrice) {
        this.stockCode = stockCode;
        this.year = year;
        this.month = month;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.totalVolume = totalVolume;
        this.totalPrice = totalPrice;
    }
}
