package com.port90.external.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class HantoCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String appKey;
    private String appSecret;

    @Column(columnDefinition = "TEXT")
    private String accessToken;
    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}