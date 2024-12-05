package com.port90.core.auth.presentation;

import com.port90.core.auth.application.MainDisplayService;
import com.port90.core.auth.dto.response.RateRank;
import com.port90.core.auth.dto.response.VolumeRank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainDisplayService mainDisplayService;

    @GetMapping("/")
    public String mainAPI() {

        return "main route";
    }

    @GetMapping("/main/rank")
    public ResponseEntity<List<VolumeRank>> displayVolumeRank() {
        List<VolumeRank> volumeRankData = mainDisplayService.getVolumeRankData();
        return ResponseEntity.ok(volumeRankData);
    }

    @GetMapping("/main/rank/rise-rate")
    public ResponseEntity<List<RateRank>> displayRiseRateRank() {
        List<RateRank> riseRateData = mainDisplayService.getRiseRateData();
        return ResponseEntity.ok(riseRateData);
    }

    @GetMapping("/main/rank/fall-rate")
    public ResponseEntity<List<RateRank>> displayFallRateRank() {
        List<RateRank> fallRateData = mainDisplayService.getFallRateData();
        return ResponseEntity.ok(fallRateData);
    }
}
