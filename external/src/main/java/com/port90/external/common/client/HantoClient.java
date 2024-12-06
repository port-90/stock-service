package com.port90.external.common.client;

import com.port90.external.common.dto.*;
import com.port90.external.domain.HantoCredential;
import com.port90.external.repository.HantoCredentialRepository;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class HantoClient {
    private final HantoCredentialRepository hantoCredentialRepository;
    private final ApiService apiService;

    @Value("${hanto.baseUrl}")
    private String BASE_URL;

    @Value("${hanto.loginUrl}")
    private String LOGIN_URL;

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

    @Transactional
    public HantoCredential login(String name) {
        HantoCredential hantoCredential = hantoCredentialRepository.findByNameIs(name);
        hantoCredential.updateAccessToken(getAccessToken(hantoCredential));
        return hantoCredential;
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
    public List<StockResponse> getMinuteChart(HantoCredential hantoCredential, String stockCode, LocalTime baseTime) {

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

    public HantoDailyChartResponse getDailyChart(String stockCode, LocalDate startDate, LocalDate endDate) {
        HantoCredential hantoCredential = hantoCredentialRepository.findFirstByUpdatedAtBeforeOrderByUpdatedAtDesc(LocalDateTime.now());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/inquire-daily-itemchartprice")
                .queryParam("FID_COND_MRKT_DIV_CODE", "J") // 주식, ETF, ETN
                .queryParam("FID_INPUT_ISCD", stockCode)  // 종목 코드
                .queryParam("FID_INPUT_DATE_1", startDate.format(dateTimeFormatter))
                .queryParam("FID_INPUT_DATE_2", endDate.format(dateTimeFormatter))
                .queryParam("FID_PERIOD_DIV_CODE", "D") // D: 최근 30거래일 / W:최근 30주 / M: 최근 30개월
                .queryParam("FID_ORG_ADJ_PRC", "0") // 수정주가 반영
                .toUriString();

        // 요청 헤더 구성
        HttpHeaders headers = buildHeaders(hantoCredential, "FHKST03010100");

        // API 호출
        ResponseEntity<HantoDailyChartResponse> response = apiService.getForObject(url, headers, HantoDailyChartResponse.class);
        log.info("[STOCK API - DAILY] {}", response.getStatusCode());

        return response.getBody();
    }

    public HantoStockResponse getStockInfo(HantoCredential hantoCredential, String stockCode) {
        return getHantoStockResponse(hantoCredential, stockCode);
    }

    public HantoStockResponse getStockInfo(String stockCode) {
        HantoCredential hantoCredential = hantoCredentialRepository.findFirstByUpdatedAtBeforeOrderByUpdatedAtDesc(LocalDateTime.now());
        return getHantoStockResponse(hantoCredential, stockCode);
    }

    private HantoStockResponse getHantoStockResponse(HantoCredential hantoCredential, String stockCode) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/search-stock-info")
                .queryParam("PRDT_TYPE_CD", "300")
                .queryParam("PDNO", stockCode)
                .toUriString();

        // 요청 헤더 구성
        HttpHeaders headers = buildHeaders(hantoCredential, "CTPF1002R");

        // API 호출
        ResponseEntity<HantoStockResponse> response = apiService.getForObject(url, headers, HantoStockResponse.class);
        log.info("[STOCK API - MINUTE] {}", response.getStatusCode());
        return response.getBody();
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
}
