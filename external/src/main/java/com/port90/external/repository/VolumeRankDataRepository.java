package com.port90.external.repository;

import com.port90.external.domain.VolumeRankData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolumeRankDataRepository extends JpaRepository<VolumeRankData, Long> {
}
