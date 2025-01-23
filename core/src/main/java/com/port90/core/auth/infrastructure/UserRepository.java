package com.port90.core.auth.infrastructure;

import com.port90.core.auth.domain.model.User;

import java.util.Map;
import java.util.Set;

public interface UserRepository {
    User save(User user);

    boolean existsById(Long userId);

    User findByUsername(String username);

    Map<Long, String> getUserIdToNameMap(Set<Long> userIds);
}
