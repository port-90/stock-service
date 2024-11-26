package com.port90.comment.common.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
        @Size(max = 300, message = "Content must not exceed 300 characters") String content,
        @Nullable Long parentId,
        @Nullable String guestPassword
) {
}
