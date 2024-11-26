package com.port90.external.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class StockResponse {
    private String stockCode;
    private String date;
    private String time;
    private String price;
    private String startPrice;
    private String highPrice;
    private String lowPrice;
    private String tradingVolume; // 체결 거래량
    private String tradingValue; // 누적거래대금
}