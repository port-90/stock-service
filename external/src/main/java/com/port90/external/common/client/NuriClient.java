package com.port90.external.common.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.port90.external.common.dto.NuriStockResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class NuriClient {
    @Value("${nuri.baseUrl}")
    private String BASE_URL;

    @Value("${nuri.key}")
    private String SERVICE_KEY;

    private final ApiService apiService;

    @SneakyThrows
    public List<NuriStockResponse> getDailyStockData(String stockCode, int size) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate baseDate = LocalDate.now();
        String url = String.format("%s/%s?serviceKey=%s&numOfRows=%d&pageNo=%d&resultType=json&endBasDt=%s&likeSrtnCd=%s",
                BASE_URL,"getStockPriceInfo", SERVICE_KEY, size, 1, baseDate.format(dateTimeFormatter), stockCode);
        URI uri = new URI(url);
        String response = apiService.getForSimpleJson(uri);
        return getResponses(response);
    }

    @SneakyThrows
    public List<NuriStockResponse> getEnableStockPriceInfo(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String url = String.format("%s/%s?serviceKey=%s&numOfRows=%d&pageNo=%d&resultType=json&basDt=%s",
                BASE_URL,"getStockPriceInfo", SERVICE_KEY, 5000, 1, date.format(dateTimeFormatter));
        URI uri = new URI(url);
        String response = apiService.getForSimpleJson(uri);
        return getResponses(response);
    }

    private List<NuriStockResponse> getResponses(String response) {
        List<NuriStockResponse> stockResponses = new ArrayList<>();
        try {
            // 응답 처리
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
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
