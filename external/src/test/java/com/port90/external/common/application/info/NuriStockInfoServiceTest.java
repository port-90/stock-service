package com.port90.external.common.application.info;

import com.port90.external.common.client.NuriClient;
import com.port90.external.common.dto.NuriStockResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class NuriStockInfoServiceTest{

    @Autowired
    StockInfoService stockInfoService;
    @Autowired
    NuriClient nuriClient;

    @Test
    void fetchStockData() {
        LocalDate today = LocalDate.now();
        List<NuriStockResponse> response = nuriClient.getEnableStockPriceInfo(today.minusDays(1));
        System.out.println(response);
        //        Set<StockInfo> saved = stockInfoRepository.findAll();
//        for (StockInfo stockInfo : stockInfos){
//            if(saved.contains(stockInfo)){
//                //update
//            } else{
//                // 거래정지나 상폐임.
//            }
//
//        }
    }

}