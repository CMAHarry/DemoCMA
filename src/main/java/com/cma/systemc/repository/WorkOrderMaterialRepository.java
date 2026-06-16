package com.cma.systemc.repository;

import com.cma.systemc.entity.WorkOrder;
import com.cma.systemc.entity.WorkOrderMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkOrderMaterialRepository extends JpaRepository<WorkOrderMaterial, Long> {
    List<WorkOrderMaterial> findByWorkOrderOrderByMaterialMaterialCodeAsc(WorkOrder workOrder);
    void deleteByWorkOrder(WorkOrder workOrder);
}
