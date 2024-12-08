package com.port90.external.common.application.chart.past;

import com.port90.external.common.dto.HantoDailyChartResponse;
import com.port90.external.common.dto.HantoDailyChartRow;
import com.port90.stockdomain.domain.chart.StockChartDaily;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class StockChartDailyMapper {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public List<StockChartDaily> toEntityList(String stockCode, HantoDailyChartResponse response) {
        List<StockChartDaily> stockChartDailyList = new ArrayList<>();
        List<HantoDailyChartRow> rows = response.getOutput2();
        for (HantoDailyChartRow row : rows) {
            StockChartDaily stockChartDaily = new StockChartDaily();
            stockChartDaily.setStockCode(stockCode);
            stockChartDaily.setDate(LocalDate.parse(row.getBusinessDate(), dateFormatter));
            stockChartDaily.setClosePrice(row.getClosingPrice());
            stockChartDaily.setOpenPrice(row.getOpeningPrice());
            stockChartDaily.setHighPrice(row.getHighPrice());
            stockChartDaily.setLowPrice(row.getLowPrice());
            stockChartDaily.setTotalVolume(row.getAccumulatedVolume());
            stockChartDaily.setTotalPrice(row.getAccumulatedTransactionAmount());
            stockChartDailyList.add(stockChartDaily);
        }

        return stockChartDailyList;
    }
}
