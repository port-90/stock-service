package com.port90.core.auth.infrastructure.impl.repository;

import com.port90.core.auth.domain.model.User;
import com.port90.core.auth.infrastructure.UserRepository;
import com.port90.core.auth.infrastructure.impl.repository.persistence.UserJpaRepository;
import com.port90.core.auth.infrastructure.impl.repository.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public List<User> findAllByIdIn(List<Long> userIds) {
        return userJpaRepository.findAllByIdIn(userIds)
                .stream()
                .map(UserMapper::toModel)
                .toList();
    }
}
