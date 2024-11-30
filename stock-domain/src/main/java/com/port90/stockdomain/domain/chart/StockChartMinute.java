package com.port90.stockdomain.domain.chart;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@ToString
@Setter
@Getter
@Entity
@IdClass(StockChartMinuteId.class)
public class StockChartMinute {

    @Id
    private String stockCode;

    @Id
    private LocalDate date;

    @Id
    private LocalTime time;
    private String price;
    private String startPrice;
    private String highPrice;
    private String lowPrice;
    private String tradingVolume; // 체결 거래량
    private String tradingValue; // 누적거래대금
}
