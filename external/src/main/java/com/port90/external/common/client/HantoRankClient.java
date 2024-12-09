package com.port90.external.common.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.port90.external.common.dto.RateRankResponse;
import com.port90.external.common.dto.VolumeRankResponse;
import com.port90.external.domain.HantoCredential;
import com.port90.external.repository.HantoCredentialRepository;
import com.port90.stockdomain.domain.rank.RankData;
import com.port90.stockdomain.infrastructure.RankDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class HantoRankClient {
    private final HantoCredentialRepository hantoCredentialRepository;
    private final RankDataRepository rankDataRepository;
    private final ApiService apiService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Value("${hanto.baseUrl}")
    private String BASE_URL;

    @Value("${hanto.loginUrl}")
    private String LOGIN_URL;

    // 거래량 순위 API 호출
    public VolumeRankResponse getVolumeRank(HantoCredential hantoCredential) {

        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/volume-rank")
                .queryParam("FID_COND_MRKT_DIV_CODE", "J") // 코스피
                .queryParam("FID_COND_SCR_DIV_CODE", "20171")
                .queryParam("FID_INPUT_ISCD", "0000") // 전체 종목
                .queryParam("FID_DIV_CLS_CODE", "0") // 전체
                .queryParam("FID_BLNG_CLS_CODE", "0") // 평균 거래량
                .queryParam("FID_TRGT_CLS_CODE", "111111111")
                .queryParam("FID_TRGT_EXLS_CLS_CODE", "0000000000")
                .queryParam("FID_INPUT_PRICE_1", "")
                .queryParam("FID_INPUT_PRICE_2", "")
                .queryParam("FID_VOL_CNT", "")
                .queryParam("FID_INPUT_DATE_1", "")
                .toUriString();

        HttpHeaders headers = buildHeaders(hantoCredential, "FHPST01710000");
        ResponseEntity<VolumeRankResponse> response = apiService.getForObject(url, headers, VolumeRankResponse.class);
        VolumeRankResponse volumeRankResponse = response.getBody();

        saveRankResponseAsJson(volumeRankResponse, RankData.RankType.VOLUME);
        log.info("API 호출 성공 및 데이터 저장 완료: {}", volumeRankResponse);

        return volumeRankResponse;
    }

    // 상승률 순위 API 호출 및 반환
    public RateRankResponse fetchAndSaveRiseRateRank(HantoCredential hantoCredential) {
        String url = buildRateRankUrl("0"); // 0: 상승률
        return fetchAndSaveRateRankData(hantoCredential, url, RankData.RankType.RISE);
    }

    // 하락률 순위 API 호출 및 반환
    public RateRankResponse fetchAndSaveFallRateRank(HantoCredential hantoCredential) {
        String url = buildRateRankUrl("1"); // 1: 하락률
        return fetchAndSaveRateRankData(hantoCredential, url, RankData.RankType.FALL);
    }

    private String buildRateRankUrl(String rankSortCode) {
        return UriComponentsBuilder.fromUriString("https://openapi.koreainvestment.com:9443")
                .path("/uapi/domestic-stock/v1/ranking/fluctuation")
                .queryParam("fid_rsfl_rate2", "") // 전체 (~비율)
                .queryParam("fid_cond_mrkt_div_code", "J") // 코스피
                .queryParam("fid_cond_scr_div_code", "20170") // Unique key
                .queryParam("fid_input_iscd", "0000") // 전체 종목
                .queryParam("fid_rank_sort_cls_code", rankSortCode) // 순위 정렬 구분 코드 (0: 상승률, 1: 하락률 등)
                .queryParam("fid_input_cnt_1", "0") // 전체 누적일수
                .queryParam("fid_prc_cls_code", "0") // 가격 구분 코드 (저가대비/고가대비)
                .queryParam("fid_input_price_1", "") // 전체 가격 (~가격)
                .queryParam("fid_input_price_2", "") // 전체 가격 (~가격)
                .queryParam("fid_vol_cnt", "") // 전체 거래량 (~거래량)
                .queryParam("fid_trgt_cls_code", "0") // 전체 대상 구분
                .queryParam("fid_trgt_exls_cls_code", "0") // 전체 제외 구분
                .queryParam("fid_div_cls_code", "0") // 전체 분류 구분
                .queryParam("fid_rsfl_rate1", "") // 전체 비율 (~비율)
                .toUriString();
    }

    private RateRankResponse fetchAndSaveRateRankData(HantoCredential hantoCredential, String url, RankData.RankType rankType) {
        HttpHeaders headers = buildHeaders(hantoCredential, "FHPST01720000");

        try {
            ResponseEntity<RateRankResponse> response = apiService.getForObject(url, headers, RateRankResponse.class);
            RateRankResponse rateRankResponse = response.getBody();

            saveRankResponseAsJson(rateRankResponse, rankType);
            log.info("{} 데이터 저장 성공: {}", rankType, rateRankResponse);
            return rateRankResponse;
        } catch (Exception e) {
            log.error("{} 데이터 저장 실패: {}", rankType, e.getMessage());
            throw new RuntimeException(rankType + " 데이터를 저장하는 중 오류가 발생했습니다.", e);
        }
    }

    private <T> void saveRankResponseAsJson(T response, RankData.RankType rankType) {
        try {
            String jsonData = objectMapper.writeValueAsString(response);

            RankData rankData = rankDataRepository.findByType(rankType)
                    .orElseGet(() -> {
                        RankData newData = new RankData();
                        newData.setType(rankType);
                        return newData;
                    });
            rankData.setResponseData(jsonData);
            rankDataRepository.save(rankData);

            log.info("JSON 데이터를 저장했습니다");
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("JSON 데이터를 저장하는 데 실패했습니다.", e);
        }
    }

    private HttpHeaders buildHeaders(HantoCredential hantoCredential, String transactionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("authorization", "Bearer " + hantoCredential.getAccessToken());
        headers.set("appkey", hantoCredential.getAppKey());
        headers.set("appsecret", hantoCredential.getAppSecret());
        headers.set("tr_id", transactionId);
        headers.set("custtype", "P");
        return headers;
    }
}
