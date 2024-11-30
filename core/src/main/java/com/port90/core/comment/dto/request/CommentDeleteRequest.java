package com.port90.core.comment.dto.request;

import jakarta.annotation.Nullable;

public record CommentDeleteRequest(
        @Nullable String guestPassword
) {
}
