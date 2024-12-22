package com.port90.core.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCommentUpdateRequest(
        @NotBlank @Size(max = 300, message = "Content must not exceed 300 characters") String content
) {
}
