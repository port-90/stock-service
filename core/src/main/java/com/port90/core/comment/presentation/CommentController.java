package com.port90.core.comment.presentation;

import com.port90.core.auth.dto.request.CustomOAuth2User;
import com.port90.core.comment.application.CommentService;
import com.port90.core.comment.dto.CommentDto;
import com.port90.core.comment.dto.request.*;
import com.port90.core.comment.dto.response.CommentUpdateResponse;
import com.port90.core.comment.dto.response.GuestCommentCreateResponse;
import com.port90.core.comment.dto.response.UserCommentCreateResponse;
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

    @PostMapping("/users")
    public UserCommentCreateResponse createUserComment(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @RequestBody @Valid UserCommentCreateRequest request
    ) {
        return commentService.createUserComment(oAuth2User.getUserId(), request);
    }

    @PostMapping("/guests")
    public GuestCommentCreateResponse createGuestComment(
            @RequestBody @Valid GuestCommentCreateRequest request
    ) {
        return commentService.createGuestComment(request);
    }

    @PatchMapping("/users/{commentId}")
    public CommentUpdateResponse updateUserComment(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @PathVariable Long commentId,
            @RequestBody @Valid UserCommentUpdateRequest request
    ) {
        return commentService.updateUserComment(oAuth2User.getUserId(), commentId, request);
    }

    @PatchMapping("/guests/{commentId}")
    public CommentUpdateResponse updateGuestComment(
            @PathVariable Long commentId,
            @RequestBody @Valid GuestCommentUpdateRequest request
    ) {
        return commentService.updateGuestComment(commentId, request);
    }

    @DeleteMapping("/users/{commentId}")
    public ResponseEntity<?> deleteUserComment(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @PathVariable Long commentId
    ) {
        commentService.deleteUserComment(oAuth2User.getUserId(), commentId);
        return ResponseEntity
                .ok()
                .body("delete success");
    }

    @DeleteMapping("/guests/{commentId}")
    public ResponseEntity<?> deleteGuestComment(
            @PathVariable Long commentId,
            @RequestBody @Valid GuestCommentDeleteRequest request
    ) {
        commentService.deleteGuestComment(commentId, request);
        return ResponseEntity
                .ok()
                .body("delete success");
    }

    @GetMapping
    public List<CommentDto> getCommentList(
            @RequestParam String stockCode,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getCommentList(stockCode, cursor, size);
    }

    @GetMapping("/minute")
    public List<CommentDto> getCommentListByMinute(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam LocalTime time,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getCommentListByMinute(stockCode, date, time, cursor, size);
    }

    @GetMapping("/hourly")
    public List<CommentDto> getCommentListByHour(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam LocalTime time,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getCommentListByHour(stockCode, date, time, cursor, size);
    }

    @GetMapping("/daily")
    public List<CommentDto> getCommentListByDaily(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getCommentListByDaily(stockCode, date, cursor, size);
    }

    @GetMapping("/weekly")
    public List<CommentDto> getCommentListByWeek(
            @RequestParam String stockCode,
            @RequestParam LocalDate date,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getCommentListByWeek(stockCode, date, cursor, size);
    }

    @GetMapping("/monthly")
    public List<CommentDto> getCommentListByMonth(
            @RequestParam String stockCode,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getCommentListByMonth(stockCode, year, month, cursor, size);
    }

    @GetMapping("/{parentId}")
    public List<CommentDto> getChildCommentList(
            @PathVariable Long parentId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "5") int size
    ) {
        return commentService.getChildCommentList(parentId, cursor, size);
    }
}
