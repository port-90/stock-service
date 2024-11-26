package com.port90.comment.infrastructure.impl.random;

import com.port90.comment.infrastructure.RandomNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SystemRandomNumberGenerator implements RandomNumberGenerator {

    private final Random random = new Random();

    @Override
    public int generate() {
        return 1000 + random.nextInt(9000); // 1000 ~ 9999
    }
}
