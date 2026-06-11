package com.cma.systemc.repository;

import com.cma.systemc.entity.IntegrationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntegrationLogRepository extends JpaRepository<IntegrationLog, Long> {
}
