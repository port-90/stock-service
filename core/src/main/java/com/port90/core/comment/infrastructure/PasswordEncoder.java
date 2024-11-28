package com.port90.core.comment.infrastructure;

// TODO: Security 의존성 추가 후 BCryptPasswordEncoder로 변경
public interface PasswordEncoder {

    String encode(String password);

    boolean matches(String rawPassword, String encodedPassword);
}
