package com.port90.stockdomain.domain.info;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class StockInfo {
    @Id
    private String stockCode;
    private String stockName;
    private Long stockCount;
    private Long marketCap;
    private String market;
    private String stockKind;

    private LocalDate openDate;
    private LocalDate closeDate;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public StockInfo(String stockCode, String stockName, Long stockCount, Long marketCap) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.stockCount = stockCount;
        this.marketCap = marketCap;
    }

    public void updateDetail(String market, String stockKind, StockStatus stockStatus, LocalDate openDate, LocalDate closeDate) {
        this.market = market;
        this.stockKind = stockKind;
        this.stockStatus = stockStatus;
        this.openDate = openDate;
        this.closeDate = closeDate;
    }
}
