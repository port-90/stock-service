package com.port90.core.auth.infrastructure.impl.repository.persistence.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
