package com.port90.core.reply.infrastructure.impl.repository.persistence.entity;

import com.port90.core.reply.domain.model.ReplyType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "Reply")
@EntityListeners(AuditingEntityListener.class)
public class ReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false)
    private Long commentId;

    @Enumerated(EnumType.STRING)
    private ReplyType type;

    private String password;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long likeCount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
