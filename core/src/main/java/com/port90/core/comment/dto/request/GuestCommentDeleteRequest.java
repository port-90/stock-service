package com.port90.core.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GuestCommentDeleteRequest(
        @NotBlank String password
) {
}
