package com.cma.systemc.repository;

import com.cma.systemc.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Optional<WorkOrder> findByWorkOrderNoIgnoreCase(String workOrderNo);
    boolean existsByWorkOrderNoIgnoreCase(String workOrderNo);
}
