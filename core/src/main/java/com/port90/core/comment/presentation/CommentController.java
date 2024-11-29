package com.port90.core.comment.presentation;

import com.port90.core.comment.application.CommentService;
import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.dto.request.CommentCreateRequest;
import com.port90.core.comment.dto.request.CommentDeleteRequest;
import com.port90.core.comment.dto.request.CommentUpdateRequest;
import com.port90.core.comment.dto.response.CommentCreateResponse;
import com.port90.core.comment.dto.response.CommentUpdatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // TODO: Security Context Holder 에서 userId 가져오는 것으로 변경
    @PostMapping(value = {"", "/{userId}"})
    public CommentCreateResponse create(
            @PathVariable(required = false) Long userId,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        return commentService.create(userId, request);
    }

    // TODO: Security Context Holder 에서 userId 가져오는 것으로 변경
    @PatchMapping(value = {"/{commentId}", "/{userId}/{commentId}"})
    public CommentUpdatedResponse update(
            @PathVariable(required = false) Long userId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        return commentService.update(userId, commentId, request);
    }

    // TODO: Security Context Holder 에서 userId 가져오는 것으로 변경
    @DeleteMapping(value = {"/{commentId}", "/{userId}/{commentId}"})
    public void delete(
            @PathVariable(required = false) Long userId,
            @PathVariable Long commentId,
            @RequestBody CommentDeleteRequest request
    ) {
        commentService.delete(userId, commentId, request);
    }

    @GetMapping("/parent")
    public List<Comment> getParentCommentList(
            @RequestParam String stockCode,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getParentCommentList(stockCode, cursor, size);
    }

    @GetMapping("/child")
    public List<Comment> getChildCommentList(
            @RequestParam Long parentId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "5") int size
    ) {
        return commentService.getChildCommentList(parentId, cursor, size);
    }
}
