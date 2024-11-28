package com.port90.core.auth.presentation;

import com.port90.core.auth.application.FavoriteStockService;
import com.port90.core.auth.dto.request.CustomOAuth2User;
import com.port90.core.auth.dto.request.FavoriteStockRequest;
import com.port90.core.auth.dto.response.FavoriteStockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite-stocks")
@RequiredArgsConstructor
public class FavoriteStockController {
    private final FavoriteStockService favoriteStockService;

    @PostMapping
    public ResponseEntity<FavoriteStockResponse> addFavoriteStock(
            @RequestBody FavoriteStockRequest request,
            @AuthenticationPrincipal CustomOAuth2User user) {
        FavoriteStockResponse response = favoriteStockService.addFavoriteStock(user.getUserId(), request);
        return ResponseEntity.status(201).body(response);
    }

    // 관심 주식 조회 API
    @GetMapping
    public ResponseEntity<List<FavoriteStockResponse>> getFavoriteStocks(
            @AuthenticationPrincipal CustomOAuth2User user) {

        List<FavoriteStockResponse> response = favoriteStockService.getFavoriteStocks(user.getUserId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFavoriteStock(@RequestParam Long userId, @RequestParam String stockCode) {
        favoriteStockService.deleteFavoriteStock(userId, stockCode);
        return ResponseEntity.noContent().build();
    }
}
