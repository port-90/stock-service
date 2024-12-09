package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.info.StockInfo;
import com.port90.stockdomain.domain.info.StockInfoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockInfoRepository extends JpaRepository<StockInfo, String> {

    @Query("SELECT s.stockCode from StockInfo s")
    List<String> findAllStockCodes();

    boolean existsByStockCode(String stockCode);

    List<StockInfo> findByStatusNot(StockInfoStatus stockInfoStatus);
}
