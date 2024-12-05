package com.port90.external.presentation;

import com.port90.external.common.client.HantoRankClient;
import com.port90.external.common.dto.RateRankResponse;
import com.port90.external.common.dto.VolumeRankResponse;
import com.port90.external.domain.HantoCredential;
import com.port90.external.repository.HantoCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stock/admin")
public class RankTestController {
    private final HantoCredentialRepository credentialRepository;
    private final HantoRankClient hantoRankClient;

    @GetMapping("/volume-rank")
    public VolumeRankResponse getVolumeRank() {
        HantoCredential credential = credentialRepository.findAll().getFirst();
        return hantoRankClient.getVolumeRank(credential);
    }

    // 상승률 데이터 호출
    @GetMapping("/rise")
    public RateRankResponse fetchRiseRateRank() {
        HantoCredential credential = credentialRepository.findAll().getFirst();
        return hantoRankClient.fetchAndSaveRiseRateRank(credential);
    }

    // 하락률 데이터 호출
    @GetMapping("/fall")
    public RateRankResponse fetchFallRateRank() {
        HantoCredential credential = credentialRepository.findAll().getFirst();
        return hantoRankClient.fetchAndSaveFallRateRank(credential);
    }
}
