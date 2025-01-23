package com.port90.core.like.infrastructure.impl.repository.persistence.entity;

import com.port90.core.like.domain.model.LikeTarget;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "Likes")
@EntityListeners(AuditingEntityListener.class)
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LikeTarget target;

    @Column(nullable = false)
    private Long targetId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
