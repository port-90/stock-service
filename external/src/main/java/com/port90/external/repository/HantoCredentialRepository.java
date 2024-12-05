package com.port90.external.repository;

import com.port90.external.domain.HantoCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HantoCredentialRepository extends JpaRepository<HantoCredential, Long> {

}
