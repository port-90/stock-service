package com.port90.external.service.info;

import java.util.List;

public interface StockInfoService {
    void fetchAndSaveAllStockInfoData();
    List<String> getAllStockCodes();
}
