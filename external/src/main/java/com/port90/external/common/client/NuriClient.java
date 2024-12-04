package com.port90.external.common.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.port90.external.common.dto.NuriStockResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Component
public class NuriClient {
    @Value("${nuri.baseUrl}")
    private String BASE_URL;

    @Value("${nuri.key}")
    private String SERVICE_KEY;

    private final ApiService apiService;

    public List<NuriStockResponse> getDailyStockData(String stockCode, int size) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate baseDate = LocalDate.now();
        URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/getStockPriceInfo")
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("numOfRows", size)
                .queryParam("pageNo", 1)
                .queryParam("resultType", "json")
                .queryParam("endBasDt", baseDate.format(dateTimeFormatter))
                .queryParam("likeSrtnCd", stockCode)
                .build().toUri();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        System.out.println(uri);
        ResponseEntity<String> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                String.class
        );
        return getResponses(response);
    }

    public List<NuriStockResponse> getStockPriceInfo(String stockCode, LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/getStockPriceInfo")
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("numOfRows", 5000)
                .queryParam("pageNo", 1)
                .queryParam("resultType", "json")
                .queryParam("basDt", date.format(dateTimeFormatter))
                .build().toUri();

        ResponseEntity<String> response = apiService.getForSimpleJson(uri);
        return getResponses(response);
    }

    private List<NuriStockResponse> getResponses(ResponseEntity<String> response) {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockResponses;
    }
}
