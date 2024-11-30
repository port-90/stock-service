package com.port90.core.auth.infrastructure;

import com.port90.core.auth.domain.model.User;

public interface UserRepository {
    User save(User user);

    User findByUsername(String username);
}
