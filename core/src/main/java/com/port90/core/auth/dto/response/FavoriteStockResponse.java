package com.port90.core.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class FavoriteStockResponse {
    private String stockCode;
    private String stockName;
    private LocalDateTime createdAt;

    private String price;
    private String startPrice;
    private String highPrice;
    private String lowPrice;
    private String tradingVolume;
    private String tradingValue;
    private LocalDate date;
    private LocalTime time;
}
