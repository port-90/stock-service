package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.rank.RateRankData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRankDataRepository extends JpaRepository<RateRankData, Long> {
}
