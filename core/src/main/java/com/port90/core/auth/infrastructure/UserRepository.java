package com.port90.core.auth.infrastructure;

import com.port90.core.auth.domain.model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    User findByUsername(String username);

    List<User> findAllByIdIn(List<Long> userIds);
}
