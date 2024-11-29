package com.port90.external.common.application.info;

import java.util.List;

public interface StockInfoService {
    void fetchAndSaveAllStockInfoData();
    List<String> getAllStockCodes();
}
