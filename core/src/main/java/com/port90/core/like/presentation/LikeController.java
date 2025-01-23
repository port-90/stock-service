package com.port90.core.like.presentation;

import com.port90.core.auth.dto.request.CustomOAuth2User;
import com.port90.core.like.application.LikeService;
import com.port90.core.like.domain.model.LikeDelete;
import com.port90.core.like.presentation.request.LikeCreateRequest;
import com.port90.core.like.presentation.response.LikeCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public LikeCreateResponse create(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @RequestBody @Valid LikeCreateRequest request
    ) {
        return LikeCreateResponse.from(
                likeService.create(request.toCommand(oAuth2User.getUserId()))
        );
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<?> delete(
            @AuthenticationPrincipal CustomOAuth2User oAuth2User,
            @PathVariable Long likeId
    ) {
        likeService.delete(
                LikeDelete.of(oAuth2User.getUserId(), likeId)
        );

        return ResponseEntity
                .ok()
                .body("delete success");
    }
}
