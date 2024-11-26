package com.port90.core.comment.application;

import com.port90.core.comment.infrastructure.RandomNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestIdGenerator {

    private static final String GUEST_ID_PREFIX = "Guest@";
    private final RandomNumberGenerator randomNumberGenerator;

    public String generate() {
        return GUEST_ID_PREFIX + randomNumberGenerator.generate();
    }
}
