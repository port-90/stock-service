package com.port90.core.like.dto.request;

import jakarta.validation.constraints.NotNull;

public record LikeCreateRequest(
        @NotNull Long commentId
) {
}
