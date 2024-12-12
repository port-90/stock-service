package com.port90.core.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.port90.core.auth.domain.exception.APIDataException;
import com.port90.core.auth.domain.exception.ErrorCode;
import com.port90.core.auth.dto.response.*;
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
                .orElseThrow(() -> new APIDataException(ErrorCode.DATA_NOT_FOUND));
        return parseResponseData(latestData, VolumeRankResponse.class).getOutput();
    }

    @Cacheable(value = "riseRateCache", key = "'rise:rate'", unless = "#result == null || #result.isEmpty()")
    public List<RateRank> getRiseRateData() {
        RankData riseRateData = rankDataRepository.findByType(RankData.RankType.RISE)
                .orElseThrow(() -> new APIDataException(ErrorCode.DATA_NOT_FOUND));
        return parseResponseData(riseRateData, RateRankResponse.class).getOutput();
    }

    @Cacheable(value = "fallRateCache", key = "'fall:rate'", unless = "#result == null || #result.isEmpty()")
    public List<RateRank> getFallRateData() {
        RankData fallRateData = rankDataRepository.findByType(RankData.RankType.FALL)
                .orElseThrow(() -> new APIDataException(ErrorCode.DATA_NOT_FOUND));
        return parseResponseData(fallRateData, RateRankResponse.class).getOutput();
    }

    @Cacheable(value = "marketCapCache", key = "'market:cap'", unless = "#result == null || #result.isEmpty()")
    public List<MarketCap> getMarketCapData() {
        RankData marketCapData = rankDataRepository.findByType(RankData.RankType.MARKET_CAP)
                .orElseThrow(() -> new APIDataException(ErrorCode.DATA_NOT_FOUND));
        return parseResponseData(marketCapData, MarketCapResponse.class).getOutput();
    }

    private <T> T parseResponseData(RankData rankData, Class<T> responseType) {
        try {
            return objectMapper.readValue(rankData.getResponseData(), responseType);
        } catch (JsonProcessingException e) {
            throw new APIDataException(ErrorCode.DATA_PARSING_ERROR);
        }
    }
}
