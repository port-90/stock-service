package com.port90.core.auth.application;

import com.port90.core.auth.dto.response.CommentResponse;
import com.port90.core.comment.domain.exception.CommentErrorCode;
import com.port90.core.comment.domain.exception.CommentException;
import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.infrastructure.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CommentRepository commentRepository;

    // 내가 작성한 댓글 조회
    public Page<CommentResponse> getCommentsByUserId(Long userId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        if (commentPage.isEmpty()) {
            throw new CommentException(CommentErrorCode.COMMENT_NOT_FOUND);
        }

        return commentPage.map(comment -> CommentResponse.builder()
                .commentId(comment.getId())
                .stockCode(comment.getStockCode())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build());
    }
}
