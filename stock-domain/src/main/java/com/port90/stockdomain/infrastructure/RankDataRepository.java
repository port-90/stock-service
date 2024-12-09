package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.rank.RankData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankDataRepository extends JpaRepository<RankData, Long> {
    Optional<RankData> findByType(RankData.RankType type);
}
