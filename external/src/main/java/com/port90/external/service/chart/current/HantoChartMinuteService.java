package com.port90.external.service.chart.current;

import com.port90.external.entity.chart.StockChartMinute;
import com.port90.external.entity.chart.StockChartMinuteId;
import com.port90.external.repository.StockChartMinuteRepository;
import com.port90.external.service.HantoClient;
import com.port90.external.service.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HantoChartMinuteService implements StockChartMinuteService {
    private final StockChartMinuteRepository stockChartMinuteRepository;
    private final HantoClient hantoClient;

    public void fetchAndSaveMinuteStockData(String stockCode) {
        LocalTime baseTime = LocalTime.now();
        List<StockResponse> stockResponses = getStockQuote(stockCode, baseTime);
        convertToDomainAndSaveAllChartMinuteData(stockResponses);
    }

    public List<StockResponse> getStockQuote(String stockCode, LocalTime baseTime) {
        // API 호출
        ResponseEntity<String> response = hantoClient.getDailyMinute(stockCode, baseTime);
        List<StockResponse> stockResponses = new ArrayList<>();
        try {
            // 응답 처리
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

    public void convertToDomainAndSaveAllChartMinuteData(List<StockResponse> stockResponses) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        for (int i =0; i<stockResponses.size(); i++) {
            StockResponse stockResponse = stockResponses.get(i);
            StockChartMinute stockChartMinute = new StockChartMinute();
            stockChartMinute.setStockCode(stockResponse.getStockCode());
            stockChartMinute.setDate(LocalDate.parse(stockResponse.getDate(), dateFormatter));
            stockChartMinute.setTime(LocalTime.parse(stockResponse.getTime(), timeFormatter));
            stockChartMinute.setPrice(stockResponse.getPrice()); // 현재가
            stockChartMinute.setStartPrice(stockResponse.getStartPrice()); // 시가
            stockChartMinute.setHighPrice(stockResponse.getHighPrice()); // 고가
            stockChartMinute.setLowPrice(stockResponse.getLowPrice()); // 저가
            stockChartMinute.setTradingVolume(stockResponse.getTradingVolume());
            stockChartMinute.setTradingValue(stockResponse.getTradingValue());

            StockChartMinuteId stockChartMinuteId = new StockChartMinuteId(stockChartMinute.getDate(), stockChartMinute.getTime(), stockChartMinute.getStockCode());
            if (stockChartMinuteRepository.existsById(stockChartMinuteId)) {
                break;
            } else {
                stockChartMinuteRepository.save(stockChartMinute);
            }
        }
    }
}
