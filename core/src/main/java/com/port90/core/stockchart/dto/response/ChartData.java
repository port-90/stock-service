package com.port90.core.stockchart.dto.response;


import java.time.LocalDate;
import java.time.LocalTime;

public record ChartData(
        LocalDate date,
        LocalTime time,
        String openPrice,
        String closePrice,
        String highPrice,
        String lowPrice,
        String totalVolume,
        String totalPrice
) {

}
