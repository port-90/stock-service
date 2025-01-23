package com.port90.core.reply.presentation;

import com.port90.core.auth.dto.request.CustomOAuth2User;
import com.port90.core.reply.application.ReplyService;
import com.port90.core.reply.domain.model.ReplyDelete;
import com.port90.core.reply.presentation.request.ReplyCreateRequest;
import com.port90.core.reply.presentation.request.ReplyDeleteRequest;
import com.port90.core.reply.presentation.request.ReplyUpdateRequest;
import com.port90.core.reply.presentation.response.ReplyCreateResponse;
import com.port90.core.reply.presentation.response.ReplyResponse;
import com.port90.core.reply.presentation.response.ReplyUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public ReplyCreateResponse create(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @RequestBody @Valid ReplyCreateRequest request
    ) {
        Long userId = oAuth2User != null ? oAuth2User.getUserId() : null;
        return ReplyCreateResponse.from(
                replyService.create(request.toCommand(userId))
        );
    }

    @PatchMapping("/{replyId}")
    public ReplyUpdateResponse update(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @PathVariable Long replyId,
            @RequestBody @Valid ReplyUpdateRequest request
    ) {
        Long userId = oAuth2User != null ? oAuth2User.getUserId() : null;
        return ReplyUpdateResponse.from(
                replyService.update(request.toCommand(userId, replyId))
        );
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> delete(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @PathVariable Long replyId,
            @RequestBody(required = false) @Valid ReplyDeleteRequest request
    ) {
        Long userId = oAuth2User != null ? oAuth2User.getUserId() : null;
        ReplyDelete replyDelete = buildReplyDeleteCommand(replyId, request, userId);
        replyService.delete(replyDelete);

        return ResponseEntity
                .ok()
                .body("delete success");
    }

    @GetMapping("/{commentId}")
    public List<ReplyResponse> getListByCommentId(
            @PathVariable Long commentId
    ) {
        return ReplyResponse.from(
                replyService.getListByCommentId(commentId)
        );
    }

    private ReplyDelete buildReplyDeleteCommand(Long replyId, ReplyDeleteRequest request, Long userId) {
        if (request == null) {
            return ReplyDelete.builder()
                    .replyId(replyId)
                    .userId(userId)
                    .password(null)
                    .build();
        }
        return request.toCommand(userId, replyId);
    }
}
