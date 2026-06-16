package com.cma.systemc.service;

import com.cma.systemc.entity.ExternalSyncStatus;
import com.cma.systemc.entity.WorkOrder;
import com.cma.systemc.entity.WorkOrderMaterial;
import com.cma.systemc.entity.WorkOrderStatus;
import com.cma.systemc.repository.WorkOrderMaterialRepository;
import com.cma.systemc.repository.WorkOrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MaterialRestockService {
    private static final Logger logger = LogManager.getLogger(MaterialRestockService.class);

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderMaterialRepository workOrderMaterialRepository;

    public MaterialRestockService(WorkOrderRepository workOrderRepository,
                                  WorkOrderMaterialRepository workOrderMaterialRepository) {
        this.workOrderRepository = workOrderRepository;
        this.workOrderMaterialRepository = workOrderMaterialRepository;
    }

    /**
     * Demo background worker.
     *
     * In a real system, this could be a different application instance, batch job, queue consumer,
     * or scheduled console application. For this demo, it runs inside Spring using @Scheduled.
     */
    @Scheduled(fixedDelayString = "${system-c.jobs.restock.fixed-delay-ms:30000}")
    @Transactional
    public void processMaterialShortageWorkOrders() {
        List<WorkOrder> shortageOrders = workOrderRepository.findByStatusOrderByCreatedAtAsc(
                WorkOrderStatus.MATERIAL_SHORTAGE);

        if (shortageOrders.isEmpty()) {
            return;
        }

        for (WorkOrder workOrder : shortageOrders) {
            logger.info("RESTOCK STARTED | WorkOrder={}", workOrder.getWorkOrderNo());

            workOrder.setStatus(WorkOrderStatus.RESTOCK_IN_PROGRESS);
            workOrder.setRemarks("Material restock is in progress.");
            workOrderRepository.save(workOrder);

            List<WorkOrderMaterial> materials = workOrderMaterialRepository
                    .findByWorkOrderOrderByMaterialMaterialCodeAsc(workOrder);

            for (WorkOrderMaterial material : materials) {
                if (Boolean.TRUE.equals(material.getRestockRequired())) {
                    // Demo restock success: make available quantity equal to required quantity.
                    material.setAvailableQty(material.getRequiredQty());
                    material.setShortageQty(BigDecimal.ZERO);
                    material.setRestockRequired(false);
                    workOrderMaterialRepository.save(material);

                    logger.info("RESTOCKED | WorkOrder={} | Material={} | NewAvailableQty={}",
                            workOrder.getWorkOrderNo(),
                            material.getMaterial().getMaterialCode(),
                            material.getAvailableQty());
                }
            }

            workOrder.setStatus(WorkOrderStatus.READY_FOR_PRODUCTION);
            workOrder.setRemarks("Restock completed successfully. Work order is ready for production.");

            // This is the trigger for the external API update worker.
            workOrder.setExternalStatusSync(ExternalSyncStatus.PENDING);
            workOrder.setExternalStatusSyncRemarks("Ready to update ISM 2 after restock success.");
            workOrderRepository.save(workOrder);

            logger.info("RESTOCK COMPLETED | WorkOrder={} | ExternalSync=PENDING", workOrder.getWorkOrderNo());
        }
    }
}
