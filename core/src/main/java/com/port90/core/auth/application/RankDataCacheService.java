package com.port90.core.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.port90.core.auth.dto.response.RateRank;
import com.port90.core.auth.dto.response.RateRankResponse;
import com.port90.core.auth.dto.response.VolumeRank;
import com.port90.core.auth.dto.response.VolumeRankResponse;
import com.port90.stockdomain.domain.rank.RankData;
import com.port90.stockdomain.infrastructure.RankDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankDataCacheService {

    private final RankDataRepository rankDataRepository;
    private final ObjectMapper objectMapper;

    @Cacheable(value = "volumeRankCache", key = "'volume:rank'", unless = "#result == null || #result.isEmpty()")
    public List<VolumeRank> getVolumeRankData() {
        RankData latestData = rankDataRepository.findByType(RankData.RankType.VOLUME)
                .orElseThrow(() -> new RuntimeException("거래량 데이터가 없습니다."));
        return parseResponseData(latestData, VolumeRankResponse.class).getOutput();
    }

    @Cacheable(value = "riseRateCache", key = "'rise:rate'", unless = "#result == null || #result.isEmpty()")
    public List<RateRank> getRiseRateData() {
        RankData riseRateData = rankDataRepository.findByType(RankData.RankType.RISE)
                .orElseThrow(() -> new RuntimeException("상승률 데이터가 없습니다."));
        return parseResponseData(riseRateData, RateRankResponse.class).getOutput();
    }

    @Cacheable(value = "fallRateCache", key = "'fall:rate'", unless = "#result == null || #result.isEmpty()")
    public List<RateRank> getFallRateData() {
        RankData fallRateData = rankDataRepository.findByType(RankData.RankType.FALL)
                .orElseThrow(() -> new RuntimeException("하락률 데이터가 없습니다."));
        return parseResponseData(fallRateData, RateRankResponse.class).getOutput();
    }

    private <T> T parseResponseData(RankData rankData, Class<T> responseType) {
        try {
            return objectMapper.readValue(rankData.getResponseData(), responseType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 데이터를 파싱하는 데 실패했습니다.", e);
        }
    }
}
