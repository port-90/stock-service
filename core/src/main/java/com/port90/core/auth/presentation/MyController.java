package com.port90.core.auth.presentation;

import com.port90.core.auth.application.UserService;
import com.port90.core.auth.dto.request.CustomOAuth2User;
import com.port90.core.auth.dto.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyController {
    private final UserService userService;

    @GetMapping("/")
    public String mainRoute() {

        return "main";
    }

    @GetMapping("/my/comments")
    public Page<CommentResponse> getMyComments(@AuthenticationPrincipal CustomOAuth2User user, Pageable pageable) {
        return userService.getCommentsByUserId(user.getUserId(), pageable);
    }
}
