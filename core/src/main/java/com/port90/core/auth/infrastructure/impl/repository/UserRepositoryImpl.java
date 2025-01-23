package com.port90.core.auth.infrastructure.impl.repository;

import com.port90.core.auth.domain.model.User;
import com.port90.core.auth.infrastructure.UserRepository;
import com.port90.core.auth.infrastructure.impl.repository.persistence.UserJpaRepository;
import com.port90.core.auth.infrastructure.impl.repository.persistence.UserMapper;
import com.port90.core.auth.infrastructure.impl.repository.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public boolean existsById(Long userId) {
        return userJpaRepository.existsById(userId);
    }

    @Override
    public User findByUsername(String username) {
        return UserMapper.toModel(jpaRepository.findByUsername(username));
    }

    @Override
    public Map<Long, String> getUserIdToNameMap(Set<Long> userIds) {
        return userJpaRepository.findAllByIdIn(userIds)
                .stream()
                .collect(Collectors.toMap(UserEntity::getId, UserEntity::getName));
    }
}
