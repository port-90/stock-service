package com.port90.external.common.client;

import com.port90.external.common.dto.HantoLoginRequest;
import com.port90.external.common.dto.HantoLoginResponse;
import com.port90.external.common.dto.StockResponse;
import com.port90.external.domain.HantoCredential;
import com.port90.external.repository.HantoCredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class HantoClient {
    @Value("${hanto.baseUrl}")
    private String BASE_URL;
    @Value("${hanto.loginUrl}")
    private String LOGIN_URL;

    private final HantoCredentialRepository hantoCredentialRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public void loginAll() {
        List<HantoCredential> hantoCredentials = hantoCredentialRepository.findAll();
        for (HantoCredential hantoCredential : hantoCredentials) {
            hantoCredential.setAccessToken(getAccessToken(hantoCredential));
        }
    }

    private String getAccessToken(HantoCredential hantoCredential) {
        String url = UriComponentsBuilder.fromUriString(LOGIN_URL)
                .toUriString();
        log.info("[URL] {}", url);

        // 요청 바디 설정
        HantoLoginRequest loginRequest = new HantoLoginRequest(hantoCredential.getAppSecret(), hantoCredential.getAppKey());

        // API 호출
        HantoLoginResponse response = restTemplate.postForObject(
                url,
                loginRequest,
                HantoLoginResponse.class
        );

        return response.getAccessToken();
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
        log.info("[URL] {}", url);

        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("authorization", "Bearer " + hantoCredential.getAccessToken());
        headers.set("appkey", hantoCredential.getAppKey());
        headers.set("appsecret", hantoCredential.getAppSecret());
        headers.set("tr_id", "FHKST03010200"); // 국내 주식 시세 조회 TR ID
        headers.set("custtype", "P");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        log.info("[STOCK API - MINUTE] {}, {}", response.getStatusCode(), response.getBody());

        return getResponses(stockCode, response);
    }

    public List<StockResponse> getResponses(String stockCode, ResponseEntity<String> response) {
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
