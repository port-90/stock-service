package com.port90.stockdomain.domain.rank;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rank_data")
@Getter
@Setter
public class RankData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RankType type;

    @Column(name = "response_data", columnDefinition = "TEXT", nullable = false)
    private String responseData;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum RankType {
        VOLUME("VOLUME"),       // 거래량
        RISE("RISE"),           // 상승률
        FALL("FALL"),           // 하락률
        MARKET_CAP("MARKET_CAP"); // 시가총액

        private final String value;

        RankType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
