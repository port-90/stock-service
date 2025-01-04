package com.port90.core.comment.dto;

public record ChildCommentCountDto(
        Long commentId,
        long childCommentCount
) {
}
