package com.port90.core.comment.infrastructure.impl.repository.persistence.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity(name = "UserComment")
@DiscriminatorValue("USER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class UserCommentEntity extends CommentEntity {

    private Long userId;

    private boolean isAnonymous;
}
