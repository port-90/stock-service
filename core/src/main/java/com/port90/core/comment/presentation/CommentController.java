package com.port90.core.comment.presentation;

import com.port90.core.auth.dto.request.CustomOAuth2User;
import com.port90.core.comment.application.CommentService;
import com.port90.core.comment.dto.CommentDto;
import com.port90.core.comment.dto.request.CommentCreateRequest;
import com.port90.core.comment.dto.request.CommentDeleteRequest;
import com.port90.core.comment.dto.request.CommentUpdateRequest;
import com.port90.core.comment.dto.response.CommentCreateResponse;
import com.port90.core.comment.dto.response.CommentUpdateResponse;
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
    public CommentCreateResponse createComment(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        Long userId = oAuth2User != null ? oAuth2User.getUserId() : null;
        return commentService.createComment(userId, request);
    }

    @PatchMapping("/{commentId}")
    public CommentUpdateResponse updateComment(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        Long userId = oAuth2User != null ? oAuth2User.getUserId() : null;
        return commentService.updateComment(userId, commentId, request);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentDeleteRequest request
    ) {
        Long userId = oAuth2User != null ? oAuth2User.getUserId() : null;
        commentService.deleteComment(userId, commentId, request);

        return ResponseEntity
                .ok()
                .body("delete success");
    }

    @GetMapping
    public List<CommentDto> getParentsByStockCode(
            @RequestParam String stockCode,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getParentsByStockCode(stockCode, cursor, size);
    }

    @GetMapping("/minute")
    public List<CommentDto> getParentsByStockChartMinute(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam LocalTime time,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getParentsByStockChartMinute(stockCode, date, time, cursor, size);
    }

    @GetMapping("/hourly")
    public List<CommentDto> getParentsByStockChartHourly(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam LocalTime time,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getParentsByStockChartHourly(stockCode, date, time, cursor, size);
    }

    @GetMapping("/daily")
    public List<CommentDto> getParentsByStockChartDaily(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getParentsByStockChartDaily(stockCode, date, cursor, size);
    }

    @GetMapping("/weekly")
    public List<CommentDto> getParentsByStockChartWeekly(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getParentsByStockChartWeekly(stockCode, date, cursor, size);
    }

    @GetMapping("/monthly")
    public List<CommentDto> getParentsByStockChartMonthly(
            @RequestParam String stockCode,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getParentsByStockChartMonthly(stockCode, year, month, cursor, size);
    }

    @GetMapping("/{parentId}")
    public List<CommentDto> getChildrenByParentId(
            @PathVariable Long parentId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "5") int size
    ) {
        return commentService.getChildrenByParentId(parentId, cursor, size);
    }
}
