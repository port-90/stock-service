package com.port90.core.comment.infrastructure.impl.fake;

import com.port90.core.comment.infrastructure.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class FakePasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String password) {
        return password;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
