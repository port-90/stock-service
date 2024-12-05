package com.port90.core.auth.application;

import com.port90.core.auth.dto.response.VolumeRank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainDisplayService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String VOLUME_RANK_KEY = "volume:rank";

    /**
     * Redis에서 거래량 순위 데이터 가져오기
     */
    @Cacheable(value = "volumeRankCache", key = "'volume:rank'")
    public List<VolumeRank> getVolumeRankData() {
        log.info("캐싱된 데이터를 조회하거나 없을 경우 기본 동작을 실행합니다.");
        throw new RuntimeException("Redis에 캐시된 거래량 순위 데이터가 없습니다.");
    }
}
