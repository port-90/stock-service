package com.port90.external.common.application.info;

import com.port90.external.domain.info.StockInfo;
import com.port90.external.infrastructure.StockInfoRepository;
import com.port90.external.common.client.NuriClient;
import com.port90.external.common.dto.NuriStockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NuriStockInfoService implements StockInfoService {

    private final StockInfoRepository stockInfoRepository;
    private final NuriClient nuriClient;

    @Override
    public void fetchAndSaveAllStockInfoData() {
        List<NuriStockResponse> stockResponses = nuriClient.getStockInfo();
        System.out.println(stockResponses);
        convertToDomainAndSaveAll(stockResponses);
    }

    @Override
    public List<String> getAllStockCodes() {
        return stockInfoRepository.findAllStockCodes();
    }

    @Transactional
    public void convertToDomainAndSaveAll(List<NuriStockResponse> stockResponses) {
        for (NuriStockResponse stockResponse : stockResponses) {
            StockInfo stockInfo = new StockInfo(
                    stockResponse.getSrtnCd(),
                    stockResponse.getItmsNm(),
                    stockResponse.getLstgStCnt(),
                    stockResponse.getMrktTotAmt());
            Optional<StockInfo> saved = stockInfoRepository.findById(stockInfo.getStockCode());
            if (saved.isPresent()) {
                StockInfo savedStockInfo = saved.get();
                savedStockInfo.setMarketCap(stockInfo.getMarketCap());
                savedStockInfo.setStockCount(stockInfo.getStockCount());
            } else {
                stockInfoRepository.save(stockInfo);
            }
        }
    }
}
