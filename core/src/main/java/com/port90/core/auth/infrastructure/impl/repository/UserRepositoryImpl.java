package com.port90.core.auth.infrastructure.impl.repository;

import com.port90.core.auth.domain.model.User;
import com.port90.core.auth.infrastructure.UserRepository;
import com.port90.core.auth.infrastructure.impl.repository.persistence.entity.UserJpaRepository;
import com.port90.core.auth.infrastructure.impl.repository.persistence.entity.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;


    @Override
    public User save(User user) {
        return UserMapper.toModel(
                userJpaRepository.save(UserMapper.toEntity(user))
        );
    }

    @Override
    public User findByUsername(String username) {
        return UserMapper.toModel(jpaRepository.findByUsername(username));
    }
}
