package com.port90.core.comment.presentation;

import com.port90.core.auth.dto.request.CustomOAuth2User;
import com.port90.core.comment.application.CommentService;
import com.port90.core.comment.domain.model.CommentDelete;
import com.port90.core.comment.presentation.request.CommentCreateRequest;
import com.port90.core.comment.presentation.request.CommentDeleteRequest;
import com.port90.core.comment.presentation.request.CommentUpdateRequest;
import com.port90.core.comment.presentation.response.CommentCreateResponse;
import com.port90.core.comment.presentation.response.CommentResponse;
import com.port90.core.comment.presentation.response.CommentUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
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
        Long userId = oAuth2User != null ? oAuth2User.getUserId() : null;
        return CommentCreateResponse.from(
                commentService.create(request.toCommand(userId))
        );
    }

    @PatchMapping("/{commentId}")
    public CommentUpdateResponse update(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        Long userId = oAuth2User != null ? oAuth2User.getUserId() : null;
        return CommentUpdateResponse.from(
                commentService.update(request.toCommand(userId, commentId))
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @PathVariable Long commentId,
            @RequestBody(required = false) @Valid CommentDeleteRequest request
    ) {
        Long userId = oAuth2User != null ? oAuth2User.getUserId() : null;
        CommentDelete commentDelete = buildCommentDeleteCommand(commentId, request, userId);
        commentService.delete(commentDelete);

        return ResponseEntity
                .ok()
                .body("delete success");
    }

    @GetMapping
    public List<CommentResponse> getListByStockCode(
            @RequestParam String stockCode,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return CommentResponse.from(
                commentService.getListByStockCode(stockCode, cursor, size)
        );
    }

    @GetMapping("/minute")
    public List<CommentResponse> getListInStockChartMinute(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam LocalTime time,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return CommentResponse.from(
                commentService.getListInStockChartMinute(stockCode, date, time, cursor, size)
        );
    }

    @GetMapping("/hourly")
    public List<CommentResponse> getListInStockChartHourly(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam LocalTime time,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return CommentResponse.from(
                commentService.getListInStockChartHourly(stockCode, date, time, cursor, size)
        );
    }

    @GetMapping("/daily")
    public List<CommentResponse> getListInStockChartDaily(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return CommentResponse.from(
                commentService.getListInStockChartDaily(stockCode, date, cursor, size)
        );
    }

    @GetMapping("/weekly")
    public List<CommentResponse> getListInStockChartWeekly(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return CommentResponse.from(
                commentService.getListInStockChartWeekly(stockCode, date, cursor, size)
        );
    }

    @GetMapping("/monthly")
    public List<CommentResponse> getListInStockChartMonthly(
            @RequestParam String stockCode,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return CommentResponse.from(
                commentService.getListInStockChartMonthly(stockCode, year, month, cursor, size)
        );
    }

    private CommentDelete buildCommentDeleteCommand(Long commentId, CommentDeleteRequest request, Long userId) {
        if (request == null) {
            return CommentDelete.builder()
                    .commentId(commentId)
                    .userId(userId)
                    .build();
        }
        return request.toCommand(userId, commentId);
    }
}
