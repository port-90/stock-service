package com.port90.stockdomain.domain.chart;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@IdClass(StockChartDailyId.class)
public class StockChartDaily {
    @Id
    private String stockCode;

    @Id
    private LocalDate date;
    private String closePrice;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String totalVolume;
    private String totalPrice;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
