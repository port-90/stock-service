package com.port90.core.auth.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class User {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static User createUser(String username, String name, String email) {
        return User.builder()
                .username(username)
                .name(name)
                .email(email)
                .role("ROLE_USER")
                .build();
    }

    public static User updateNameAndEmail(String name, String email) {
        return User.builder()
                .name(name)
                .email(email)
                .build();
    }
}
