package com.port90.external.infrastructure;

import com.port90.external.domain.info.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockInfoRepository extends JpaRepository<StockInfo, String> {

    @Query("SELECT s.stockCode from StockInfo s")
    List<String> findAllStockCodes();
}
