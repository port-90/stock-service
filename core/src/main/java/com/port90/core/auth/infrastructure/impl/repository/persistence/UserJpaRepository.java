package com.port90.core.auth.infrastructure.impl.repository.persistence;

import com.port90.core.auth.infrastructure.impl.repository.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    List<UserEntity> findAllByIdIn(@Param("userIds") List<Long> userIds);
}
