package com.port90.core.auth.application;

import com.port90.core.auth.dto.response.MarketCap;
import com.port90.core.auth.dto.response.RateRank;
import com.port90.core.auth.dto.response.VolumeRank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainDisplayService {

    private final RankDataCacheService rankDataCacheService;

    public List<VolumeRank> getVolumeRankData(int limit) {
        List<VolumeRank> cachedData = rankDataCacheService.getVolumeRankData();
        return cachedData.subList(0, Math.min(limit, cachedData.size()));
    }

    public List<RateRank> getRiseRateData(int limit) {
        List<RateRank> cachedData = rankDataCacheService.getRiseRateData();
        return cachedData.subList(0, Math.min(limit, cachedData.size()));
    }

    public List<RateRank> getFallRateData(int limit) {
        List<RateRank> cachedData = rankDataCacheService.getFallRateData();
        return cachedData.subList(0, Math.min(limit, cachedData.size()));
    }

    public List<MarketCap> getMarketCapData(int limit) {
        List<MarketCap> cachedData = rankDataCacheService.getMarketCapData();
        return cachedData.subList(0, Math.min(limit, cachedData.size()));
    }
}