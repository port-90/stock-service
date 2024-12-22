package com.port90.stockdomain.domain.info;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = "stockCode")
@Entity
public class StockInfo {
    @Id
    private String stockCode;
    private String stockName;
    private long stockCount; // 상장주식수
    private long marketCap; // 시가총액
    private String market; // 코스피, 코스닥
    private String stockKind; // 그룹코드
    private String stopStatus; // 거래정지여부
    private String lockStatus; // 락 구분

    @Enumerated(EnumType.STRING)
    private StockInfoStatus status;
    private LocalDate openDate;
    private LocalDate closeDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void updateStockInfo(LocalDate closeDate, StockInfoStatus status) {
        this.closeDate = closeDate;
        this.status = status;
    }
}
