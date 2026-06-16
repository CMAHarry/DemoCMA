package com.cma.systemc.dto;

import com.cma.systemc.entity.ExternalSyncStatus;
import com.cma.systemc.entity.WorkOrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public class WorkOrderResponse {
    private String workOrderNo;
    private String goodsRequestNo;
    private String orderNo;
    private String productCode;
    private String productName;
    private Integer quantity;
    private WorkOrderStatus status;
    private String remarks;
    private ExternalSyncStatus externalStatusSync;
    private LocalDateTime externalStatusSyncedAt;
    private String externalStatusSyncRemarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<WorkOrderMaterialResponse> requiredMaterials;

    public String getWorkOrderNo() { return workOrderNo; }
    public void setWorkOrderNo(String workOrderNo) { this.workOrderNo = workOrderNo; }

    public String getGoodsRequestNo() { return goodsRequestNo; }
    public void setGoodsRequestNo(String goodsRequestNo) { this.goodsRequestNo = goodsRequestNo; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrderStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public ExternalSyncStatus getExternalStatusSync() { return externalStatusSync; }
    public void setExternalStatusSync(ExternalSyncStatus externalStatusSync) { this.externalStatusSync = externalStatusSync; }

    public LocalDateTime getExternalStatusSyncedAt() { return externalStatusSyncedAt; }
    public void setExternalStatusSyncedAt(LocalDateTime externalStatusSyncedAt) { this.externalStatusSyncedAt = externalStatusSyncedAt; }

    public String getExternalStatusSyncRemarks() { return externalStatusSyncRemarks; }
    public void setExternalStatusSyncRemarks(String externalStatusSyncRemarks) { this.externalStatusSyncRemarks = externalStatusSyncRemarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<WorkOrderMaterialResponse> getRequiredMaterials() { return requiredMaterials; }
    public void setRequiredMaterials(List<WorkOrderMaterialResponse> requiredMaterials) { this.requiredMaterials = requiredMaterials; }
}
