package com.port90.core.comment.presentation;

import com.port90.core.auth.dto.request.CustomOAuth2User;
import com.port90.core.comment.application.CommentService;
import com.port90.core.comment.dto.CommentDto;
import com.port90.core.comment.dto.request.CommentCreateRequest;
import com.port90.core.comment.dto.request.CommentDeleteRequest;
import com.port90.core.comment.dto.request.CommentUpdateRequest;
import com.port90.core.comment.dto.response.CommentCreateResponse;
import com.port90.core.comment.dto.response.CommentUpdatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentCreateResponse create(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        Long userId = (oAuth2User != null) ? oAuth2User.getUserId() : null;
        return commentService.create(userId, request);
    }

    @PatchMapping("/{commentId}")
    public CommentUpdatedResponse update(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        Long userId = (oAuth2User != null) ? oAuth2User.getUserId() : null;
        return commentService.update(userId, commentId, request);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @PathVariable Long commentId,
            @RequestBody CommentDeleteRequest request
    ) {
        Long userId = (oAuth2User != null) ? oAuth2User.getUserId() : null;
        commentService.delete(userId, commentId, request);
        return ResponseEntity
                .ok()
                .body("delete success");
    }

    @GetMapping("/parent")
    public List<CommentDto> getParentCommentList(
            @RequestParam String stockCode,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getParentCommentList(stockCode, cursor, size);
    }

    @GetMapping("/child")
    public List<CommentDto> getChildCommentList(
            @RequestParam Long parentId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "5") int size
    ) {
        return commentService.getChildCommentList(parentId, cursor, size);
    }
}
