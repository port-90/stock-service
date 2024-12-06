package com.port90.external.common.application.info;

import com.port90.external.common.client.HantoClient;
import com.port90.external.common.dto.HantoStockResponse;
import com.port90.stockdomain.domain.info.StockInfo;
import com.port90.stockdomain.domain.info.StockInfoStatus;
import com.port90.stockdomain.infrastructure.StockInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StockInfoService {

    private final StockInfoRepository stockInfoRepository;
    private final HantoClient hantoClient;

    @Transactional
    public void updateStockInfoWithDetail() {
        List<StockInfo> stockInfoList = stockInfoRepository.findAll();
        int index = 0;
        try {
            for (StockInfo stockInfo : stockInfoList) {
                HantoStockResponse hantoStockResponse = hantoClient.getStockInfo(stockInfo.getStockCode());
                updateDetail(stockInfo, hantoStockResponse);
                index++;
                if (index % 15 == 0) Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDetail(StockInfo stockInfo, HantoStockResponse hantoStockResponse) {
        HantoStockResponse.Output output = hantoStockResponse.getOutput();
        LocalDate closeDate = null;
        String closeDateStr = output.getLstgAbolDt();
        if (!closeDateStr.isEmpty()) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            closeDate = LocalDate.parse(closeDateStr, dateTimeFormatter);
        }

        stockInfo.updateStockInfo(closeDate,makeStatus(output));
    }

    private StockInfoStatus makeStatus(HantoStockResponse.Output output) {
        String closeDate = output.getLstgAbolDt();
        if (!closeDate.isEmpty()) return StockInfoStatus.CLOSE;
        if (output.getTrStopYn().equals("Y")) return StockInfoStatus.STOP;
        return StockInfoStatus.OPEN;
    }
}
