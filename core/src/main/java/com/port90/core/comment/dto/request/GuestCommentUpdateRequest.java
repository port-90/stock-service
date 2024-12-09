package com.port90.core.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GuestCommentUpdateRequest(
        @NotBlank @Size(max = 300, message = "Content must not exceed 300 characters") String content,
        @NotBlank String password
) {
}
