package com.port90.core.comment.infrastructure.impl.repository.persistence.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity(name = "GuestComment")
@DiscriminatorValue("GUEST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class GuestCommentEntity extends CommentEntity {

    private String password;
}
