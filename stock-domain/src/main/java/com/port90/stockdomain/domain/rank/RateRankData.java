package com.port90.stockdomain.domain.rank;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rate_rank_data")
@Getter
@Setter
public class RateRankData {
    @Id
    private Long id;   // 1: 상승률, 2: 하락률

    @Column(name = "response_data", columnDefinition = "TEXT", nullable = false)
    private String responseData;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
