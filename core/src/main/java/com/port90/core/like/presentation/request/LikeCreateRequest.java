package com.port90.core.like.presentation.request;

import com.port90.core.like.domain.model.LikeCreate;
import com.port90.core.like.domain.model.LikeTarget;
import jakarta.validation.constraints.NotNull;

public record LikeCreateRequest(
        @NotNull LikeTarget target,
        @NotNull Long targetId
) {
    public LikeCreate toCommand(Long userId) {
        return LikeCreate.builder()
                .userId(userId)
                .target(this.target)
                .targetId(this.targetId)
                .build();
    }
}
