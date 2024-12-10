package com.port90.stockdomain.domain.chart;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "stock_chart_hourly")
@Getter
@IdClass(StockChartHourlyId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockChartHourly {

    @Id
    @Column(nullable = false)
    private LocalDate date;
    @Id
    @Column(nullable = false)
    private LocalTime time;
    @Id
    @Column(nullable = false)
    private String stockCode;

    @Column(nullable = false)
    private String openPrice;
    @Column(nullable = false)
    private String closePrice;
    @Column(nullable = false)
    private String highPrice;
    @Column(nullable = false)
    private String lowPrice;
    @Column(nullable = false)
    private String tradingValue;
    @Column(nullable = false)
    private String tradingVolume;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    @SuppressWarnings({"unused", "java:S107"})
    public StockChartHourly(LocalDate date,
            LocalTime time,
            String stockCode,
            String openPrice,
            String closePrice,
            String highPrice,
            String lowPrice,
            String tradingValue,
            String tradingVolume) {
        this.date = date;
        this.time = time;
        this.stockCode = stockCode;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.tradingValue = tradingValue;
        this.tradingVolume = tradingVolume;
    }
}
