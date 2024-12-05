package com.port90.external.common.application.chart.current;

import com.port90.external.common.client.HantoClient;
import com.port90.external.common.dto.VolumeRank;
import com.port90.external.common.dto.VolumeRankResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainRankService {
    private final HantoClient hantoClient;
    private final RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = "volumeRankCache", key = "'volume:rank'")
    public List<VolumeRank> fetchVolumeRank() {
        VolumeRankResponse response = hantoClient.getVolumeRank();

        if (response == null || response.getOutput() == null) {
            log.warn("API 호출 결과가 비어 있습니다.");
            throw new RuntimeException("거래량 순위 데이터를 가져오지 못했습니다.");
        }

        return response.getOutput(); // 반환값이 자동으로 Redis에 저장됩니다.
    }
}
