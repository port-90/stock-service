package com.port90.external.common.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.port90.external.common.dto.HantoLoginRequest;
import com.port90.external.common.dto.HantoLoginResponse;
import com.port90.external.common.dto.StockResponse;
import com.port90.external.common.dto.VolumeRankResponse;
import com.port90.external.domain.HantoCredential;
import com.port90.external.repository.HantoCredentialRepository;
import com.port90.stockdomain.domain.rank.VolumeRankData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class HantoClient {
    private final HantoCredentialRepository hantoCredentialRepository;
    private final com.port90.stockdomain.infrastructure.VolumeRankDataRepository volumeRankDataRepository;
    private final ApiService apiService;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Value("${hanto.baseUrl}")
    private String BASE_URL;

    @Value("${hanto.loginUrl}")
    private String LOGIN_URL;

    @Cacheable(value = "credential", key = "'all'")
    public List<HantoCredential> getCredentials() {
        return hantoCredentialRepository.findAll();
    }

    @Transactional
    public List<HantoCredential> loginAll() {
        List<HantoCredential> hantoCredentials = hantoCredentialRepository.findAll();
        for (HantoCredential hantoCredential : hantoCredentials) {
            hantoCredential.updateAccessToken(getAccessToken(hantoCredential));
        }
        return hantoCredentials;
    }

    // 국내 휴장일조회
    @Cacheable(value = "holiday", key = "#baseDate")
    public String isHoliday(HantoCredential hantoCredential, LocalDate baseDate) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowStr = baseDate.format(timeFormatter);
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/chk-holiday")
                .queryParam("BASS_DT", nowStr)
                .queryParam("CTX_AREA_NK", "")
                .queryParam("CTX_AREA_FK", "")
                .toUriString();

        // 요청 헤더 구성
        HttpHeaders headers = buildHeaders(hantoCredential, "CTCA0903R");

        // API 호출
        ResponseEntity<String> response = apiService.getForObject(url, headers, String.class);
        log.info("[STOCK API - HOLIDAY] {}, {}", response.getStatusCode(), response.getBody());

        return getHolidayResponses(response);
    }

    // 주식당일분봉조회
    // stockCode: 종목 단축코드
    // baseTime: 해당시간 이전 1분간 30분 데이터 반환
    public List<StockResponse> getDailyMinute(HantoCredential hantoCredential, String stockCode, LocalTime baseTime) {

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        String nowStr = baseTime.format(timeFormatter);
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/inquire-time-itemchartprice")
                .queryParam("FID_ETC_CLS_CODE", ",")
                .queryParam("FID_COND_MRKT_DIV_CODE", "J") // 코스피
                .queryParam("FID_INPUT_ISCD", stockCode)  // 종목 코드
                .queryParam("FID_INPUT_HOUR_1", nowStr)
                .queryParam("FID_PW_DATA_INCU_YN", "N")
                .toUriString();

        // 요청 헤더 구성
        HttpHeaders headers = buildHeaders(hantoCredential, "FHKST03010200");

        // API 호출
        ResponseEntity<String> response = apiService.getForObject(url, headers, String.class);
        log.info("[STOCK API - MINUTE] {}", response.getStatusCode());

        return getDailyMintueResponses(stockCode, response);
    }

    private String getAccessToken(HantoCredential hantoCredential) {
        HantoLoginRequest loginRequest = new HantoLoginRequest(hantoCredential.getAppSecret(), hantoCredential.getAppKey());
        HantoLoginResponse response = apiService.postForObject(LOGIN_URL, loginRequest, HantoLoginResponse.class);
        log.info("[HANTO LOGIN] {}", response);
        return response.getAccessToken();
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

    private String getHolidayResponses(ResponseEntity<String> response) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
            JSONArray list = (JSONArray) jsonObject.get("output");
            JSONObject first = (JSONObject) list.getFirst();

            String baseDateTime = (String) first.get("bass_dt");
            String isOpen = (String) first.get("opnd_yn");
            log.info("[CHK_HOLIDAY] {}: {}", baseDateTime, isOpen);
            return isOpen;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<StockResponse> getDailyMintueResponses(String stockCode, ResponseEntity<String> response) {
        List<StockResponse> stockResponses = new ArrayList<>();
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
            JSONArray list = (JSONArray) jsonObject.get("output2");

            for (Object o : list) {
                JSONObject output = (JSONObject) o;
                StockResponse stockResponse = new StockResponse();
                stockResponse.setStockCode(stockCode);
                stockResponse.setDate((String) output.get("stck_bsop_date"));
                stockResponse.setTime((String) output.get("stck_cntg_hour"));
                stockResponse.setPrice((String) output.get("stck_prpr")); // 현재가
                stockResponse.setStartPrice((String) output.get("stck_oprc")); // 시가
                stockResponse.setHighPrice((String) output.get("stck_hgpr")); // 고가
                stockResponse.setLowPrice((String) output.get("stck_lwpr")); // 저가
                stockResponse.setTradingVolume((String) output.get("cntg_vol"));  // 누적 거래량
                stockResponse.setTradingValue((String) output.get("acml_tr_pbmn"));  // 누적 거래량

                stockResponses.add(stockResponse);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stockResponses;
    }

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

        saveVolumeRankResponseAsJson(volumeRankResponse);
        log.info("API 호출 성공 및 데이터 저장 완료: {}", volumeRankResponse);

        return volumeRankResponse;
    }

    private void saveVolumeRankResponseAsJson(VolumeRankResponse response) {
        try {
            String jsonData = objectMapper.writeValueAsString(response);

            VolumeRankData volumeRankData = new VolumeRankData();
            volumeRankData.setResponseData(jsonData);
            volumeRankDataRepository.save(volumeRankData);

            log.info("거래량 순위 데이터를 JSON으로 저장했습니다: {}", jsonData);
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("거래량 순위 데이터를 JSON으로 저장하는 데 실패했습니다.");
        }
    }
}
