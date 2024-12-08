package com.port90.core.comment.infrastructure.impl.repository.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "Comment")
@EntityListeners(AuditingEntityListener.class)
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stockCode;

    private Long userId;

    private String guestPassword;

    @Column(nullable = false)
    private String content;

    private Long parentId;

    private int likeCount;

    private boolean isParent;

    private boolean isChild;

    private boolean isUserComment;

    private boolean isAnonymousUserComment;

    private boolean isGuestComment;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
