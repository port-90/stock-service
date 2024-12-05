package com.port90.external.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "volume_rank_data")
@Getter
@Setter
public class VolumeRankData {
    @Id
    private Long id = 1L;

    @Column(name = "response_data", columnDefinition = "TEXT", nullable = false)
    private String responseData; // JSON 데이터를 저장

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
