package com.port90.external.service;

import com.port90.external.service.dto.HantoLoginRequest;
import com.port90.external.service.dto.HantoLoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class HantoClient {
    @Value("${hanto.appkey}")
    private String APP_KEY;
    @Value("${hanto.appsecret}")
    private String APP_SECRET;
    @Value("${hanto.baseUrl}")
    private String BASE_URL;
    @Value("${hanto.loginUrl}")
    private String LOGIN_URL;

    private String accessToken;
    private final RestTemplate restTemplate = new RestTemplate();

    public void login() {
        // cosntruct 에서 하려고 하면 안됨.
        // 순서 : constructor 호출 -> porperty 파일 @value로 삽입 임.
        this.accessToken = getAccessToken();
    }

    private String getAccessToken() {
        String url = UriComponentsBuilder.fromUriString(LOGIN_URL)
                .toUriString();
        log.info("[URL] {}", url);

        // 요청 바디 설정
        HantoLoginRequest loginRequest = new HantoLoginRequest(APP_SECRET, APP_KEY);

        // API 호출
        HantoLoginResponse response = restTemplate.postForObject(
                url,
                loginRequest,
                HantoLoginResponse.class
        );

        System.out.println(response);
        return response.getAccessToken();
    }

    // 주식당일분봉조회
    // stockCode: 종목 단축코드
    // baseTime: 해당시간 이전 1분간 30분 데이터 반환
    public ResponseEntity<String> getDailyMinute(String stockCode, LocalTime baseTime) {
        if (this.accessToken == null) login();

        LocalTime now = baseTime;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        String nowStr = now.format(timeFormatter);
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
        headers.set("authorization", "Bearer " + accessToken);
        headers.set("appkey", APP_KEY);
        headers.set("appsecret", APP_SECRET);
        headers.set("tr_id", "FHKST03010200"); // 국내 주식 시세 조회 TR ID
        headers.set("custtype", "P");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // API 호출
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
    }

}
