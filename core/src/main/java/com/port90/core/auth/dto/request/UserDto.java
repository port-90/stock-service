package com.port90.core.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long userId;
    private String role;
    private String name;
    private String username;
}