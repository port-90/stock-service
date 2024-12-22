package com.port90.core.auth.presentation;

import com.port90.core.auth.application.MainDisplayService;
import com.port90.core.auth.dto.response.MarketCap;
import com.port90.core.auth.dto.response.RateRank;
import com.port90.core.auth.dto.response.VolumeRank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranks")
public class MainController {
    private final MainDisplayService mainDisplayService;

    @GetMapping("/volumes")
    public ResponseEntity<List<VolumeRank>> displayVolumeRank(@RequestParam(defaultValue = "10") int limit) {
        List<VolumeRank> volumeRankData = mainDisplayService.getVolumeRankData(limit);
        return ResponseEntity.ok(volumeRankData);
    }

    @GetMapping("/rise")
    public ResponseEntity<List<RateRank>> displayRiseRateRank(@RequestParam(defaultValue = "10") int limit) {
        List<RateRank> riseRateData = mainDisplayService.getRiseRateData(limit);
        return ResponseEntity.ok(riseRateData);
    }

    @GetMapping("/fall")
    public ResponseEntity<List<RateRank>> displayFallRateRank(@RequestParam(defaultValue = "10") int limit) {
        List<RateRank> fallRateData = mainDisplayService.getFallRateData(limit);
        return ResponseEntity.ok(fallRateData);
    }

    @GetMapping("/market-cap")
    public ResponseEntity<List<MarketCap>> displayMarketCapRank(@RequestParam(defaultValue = "10") int limit) {
        List<MarketCap> marketCapData = mainDisplayService.getMarketCapData(limit);
        return ResponseEntity.ok(marketCapData);
    }
}
