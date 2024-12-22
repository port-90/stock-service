package com.port90.external.scheduler;

import com.port90.external.common.client.HantoClient;
import com.port90.external.common.client.HantoRankClient;
import com.port90.external.domain.HantoCredential;
import com.port90.external.repository.HantoCredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankScheduler {
    private final HantoRankClient hantoRankClient;
    private final HantoClient hantoClient;
    private final HantoCredentialRepository credentialRepository;

    @Scheduled(cron = "0 * 9-15 * * 1-5")
    public void fetchAndSaveRankData() {
        HantoCredential credential = credentialRepository.findAll().getFirst();
        String result = hantoClient.isHoliday(credential, LocalDate.now());
        if (result.equals("N")) return;

        try {
            hantoRankClient.getVolumeRank(credential);
            hantoRankClient.fetchAndSaveRiseRateRank(credential);
            hantoRankClient.fetchAndSaveFallRateRank(credential);
            hantoRankClient.fetchMarketCapRanking(credential);
        } catch (Exception e) {
            log.error("Rank 데이터 업데이트 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
