package com.cma.systemc.service;

import com.cma.systemc.dto.ExternalStatusUpdateRequest;
import com.cma.systemc.entity.ExternalSyncStatus;
import com.cma.systemc.entity.WorkOrder;
import com.cma.systemc.repository.WorkOrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExternalStatusSyncService {
    private static final Logger logger = LogManager.getLogger(ExternalStatusSyncService.class);

    private final WorkOrderRepository workOrderRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${system-c.integration.ism2-status-update-url:http://localhost:8083/mock/ism2/order-product-status}")
    private String ism2StatusUpdateUrl;

    public ExternalStatusSyncService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    /**
     * Demo external API sync worker.
     *
     * It picks up work orders where externalStatusSync = PENDING,
     * then calls the ISM 2 status update API.
     */
    @Scheduled(fixedDelayString = "${system-c.jobs.external-sync.fixed-delay-ms:30000}")
    @Transactional
    public void syncPendingStatusToIsm2() {
        List<WorkOrder> pendingOrders = workOrderRepository.findByExternalStatusSyncOrderByUpdatedAtAsc(
                ExternalSyncStatus.PENDING);

        if (pendingOrders.isEmpty()) {
            return;
        }

        for (WorkOrder workOrder : pendingOrders) {
            ExternalStatusUpdateRequest request = buildExternalStatusRequest(workOrder);

            try {
                logger.info("EXTERNAL SYNC STARTED | WorkOrder={} | Status={} | Url={}",
                        workOrder.getWorkOrderNo(), workOrder.getStatus(), ism2StatusUpdateUrl);

                restTemplate.postForObject(ism2StatusUpdateUrl, request, String.class);

                workOrder.setExternalStatusSync(ExternalSyncStatus.SUCCESS);
                workOrder.setExternalStatusSyncedAt(LocalDateTime.now());
                workOrder.setExternalStatusSyncRemarks("ISM 2 status update success.");
                workOrderRepository.save(workOrder);

                logger.info("EXTERNAL SYNC SUCCESS | WorkOrder={} | Status={}",
                        workOrder.getWorkOrderNo(), workOrder.getStatus());
            } catch (Exception ex) {
                workOrder.setExternalStatusSync(ExternalSyncStatus.FAILED);
                workOrder.setExternalStatusSyncedAt(LocalDateTime.now());
                workOrder.setExternalStatusSyncRemarks("ISM 2 status update failed: " + ex.getMessage());
                workOrderRepository.save(workOrder);

                logger.error("EXTERNAL SYNC FAILED | WorkOrder={} | Error={}",
                        workOrder.getWorkOrderNo(), ex.getMessage(), ex);
            }
        }
    }

    private ExternalStatusUpdateRequest buildExternalStatusRequest(WorkOrder workOrder) {
        ExternalStatusUpdateRequest request = new ExternalStatusUpdateRequest();
        request.setWorkOrderNo(workOrder.getWorkOrderNo());
        request.setGoodsRequestNo(workOrder.getGoodsRequestNo());
        request.setOrderNo(workOrder.getOrderNo());
        request.setExternalReferenceNo(workOrder.getExternalReferenceNo());
        request.setStatus(workOrder.getStatus().name());
        request.setRemarks(workOrder.getRemarks());
        return request;
    }
}
