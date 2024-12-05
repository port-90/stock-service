package com.port90.core.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.port90.core.auth.dto.response.VolumeRank;
import com.port90.core.auth.dto.response.VolumeRankResponse;
import com.port90.stockdomain.domain.rank.VolumeRankData;
import com.port90.stockdomain.infrastructure.VolumeRankDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainDisplayService {

    private final VolumeRankDataRepository volumeRankDataRepository;
    private final ObjectMapper objectMapper;

    private static final String VOLUME_RANK_CACHE = "volumeRankCache";

    /**
     * Redis에서 거래량 순위 데이터 가져오기
     */
    @Cacheable(value = VOLUME_RANK_CACHE, key = "'volume:rank'", unless = "#result == null || #result.isEmpty()")
    public List<VolumeRank> getVolumeRankData() {
        log.info("Redis 캐시 미스 발생, DB에서 데이터를 가져옵니다.");

        // DB에서 데이터 가져오기
        VolumeRankData latestData = volumeRankDataRepository.findAll().getFirst();

        try {
            // JSON 데이터를 DTO 리스트로 변환
            VolumeRankResponse volumeRankResponse = objectMapper.readValue(latestData.getResponseData(), VolumeRankResponse.class);
            return volumeRankResponse.getOutput();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("DB에서 가져온 JSON 데이터를 파싱하는 데 실패했습니다.", e);
        }
    }
}