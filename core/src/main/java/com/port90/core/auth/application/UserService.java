package com.port90.core.auth.application;

import com.port90.core.auth.dto.response.CommentResponse;
import com.port90.core.comment.application.CommentService;
import com.port90.core.comment.domain.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CommentService commentService;

    // 내가 작성한 댓글 조회
    public Page<CommentResponse> getCommentsByUserId(Long userId, Pageable pageable) {
        Page<Comment> commentPage = commentService.getListByUserId(userId, pageable);

        return commentPage.map(comment -> CommentResponse.builder()
                .commentId(comment.getId())
                .stockCode(comment.getStockChartMinuteId().stockCode())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build());
    }
}
