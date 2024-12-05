package com.port90.stockdomain.infrastructure;

import com.port90.stockdomain.domain.rank.VolumeRankData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolumeRankDataRepository extends JpaRepository<VolumeRankData, Long> {
}