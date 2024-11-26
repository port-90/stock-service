package com.port90.comment.infrastructure.impl.encoder;

import com.port90.comment.infrastructure.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class FakePasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String password) {
        return password;
    }
}
