package com.cma.systemc.repository;

import com.cma.systemc.entity.ExternalSyncStatus;
import com.cma.systemc.entity.WorkOrder;
import com.cma.systemc.entity.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Optional<WorkOrder> findByWorkOrderNoIgnoreCase(String workOrderNo);
    boolean existsByWorkOrderNoIgnoreCase(String workOrderNo);

    List<WorkOrder> findByStatusOrderByCreatedAtAsc(WorkOrderStatus status);
    List<WorkOrder> findByExternalStatusSyncOrderByUpdatedAtAsc(ExternalSyncStatus externalStatusSync);
}
