package com.port90.core.comment.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(
        @Size(max = 300, message = "Content must not exceed 300 characters") String content,
        @Nullable String guestPassword
) {
}
