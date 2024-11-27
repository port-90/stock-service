package com.port90.core.auth.infrastructure.impl.repository.persistence.entity;

import com.port90.core.auth.domain.model.User;

import java.util.Optional;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static User toModel(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .role(userEntity.getRole())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    public static Optional<User> toModel(Optional<UserEntity> optionalUserEntity) {
        return optionalUserEntity.map(UserMapper::toModel);
    }
}
