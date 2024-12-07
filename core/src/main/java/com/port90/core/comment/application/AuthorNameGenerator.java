package com.port90.core.comment.application;

import com.port90.core.comment.infrastructure.RandomNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorNameGenerator {

    private static final String AUTHOR_NAME_PREFIX = "익명@";

    private final RandomNumberGenerator randomNumberGenerator;

    public String generate() {
        return AUTHOR_NAME_PREFIX + randomNumberGenerator.generate();
    }
}
