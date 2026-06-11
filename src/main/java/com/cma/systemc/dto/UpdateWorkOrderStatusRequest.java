package com.cma.systemc.dto;

import com.cma.systemc.entity.WorkOrderStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateWorkOrderStatusRequest {
    @NotNull
    private WorkOrderStatus status;

    private String remarks;

    public WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrderStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
