package com.port90.comment.presentation;

import com.port90.comment.application.CommentService;
import com.port90.comment.common.dto.request.CommentCreateRequest;
import com.port90.comment.common.dto.response.CommentCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
