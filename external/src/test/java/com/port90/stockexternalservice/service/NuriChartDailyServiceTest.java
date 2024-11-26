package com.port90.stockexternalservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.port90.external.service.dto.NuriStockResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

class NuriChartDailyServiceTest {

    private String BASE_URL;
    private String SERVICE_KEY;

    @BeforeEach
    void setUp() throws IOException {
        // Load application.yml from classpath
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        PropertySource<?> propertySource = loader.load(
                "application.yml",
                new ClassPathResource("application.yml")
        ).get(0); // 첫 번째 PropertySource 가져오기

        // 특정 키의 값 읽기

        this.BASE_URL = (String) propertySource.getProperty("nuri.baseUrl");
        this.SERVICE_KEY = (String) propertySource.getProperty("nuri.key");
    }

    @Test
    void connectNuri() {

        URI url = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/getStockPriceInfo")
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("numOfRows", 100)
                .queryParam("pageNo", 1)
                .queryParam("resultType", "json")
                .queryParam("endBasDt", "20241124")
                .queryParam("likeSrtnCd", "005930")
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        List<NuriStockResponse> stockResponses = new ArrayList<>();
        try {
            // 응답 처리
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
            JSONObject responseObj = (JSONObject) jsonObject.get("response");
            JSONObject bodyObj = (JSONObject) responseObj.get("body");
            JSONObject itemsObj = (JSONObject) bodyObj.get("items");
            JSONArray list = (JSONArray) itemsObj.get("item");

            ObjectMapper objectMapper = new ObjectMapper();
            for (Object o : list) {
                JSONObject output = (JSONObject) o;
                String jsonString = output.toJSONString();
                NuriStockResponse stock = objectMapper.readValue(jsonString, NuriStockResponse.class);
                stockResponses.add(stock);
                System.out.println(stock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}