package com.cma.systemc.api;

import com.cma.systemc.dto.*;
import com.cma.systemc.entity.WorkOrder;
import com.cma.systemc.service.IntegrationLogService;
import com.cma.systemc.service.WorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/integration/work-orders")
@Tag(name = "ISM 2 Integration API", description = "API used by ISM 2 to create work orders and retrieve/update manufacturing status")
public class ManufacturingIntegrationApiController {
    private final WorkOrderService workOrderService;
    private final IntegrationLogService integrationLogService;

    public ManufacturingIntegrationApiController(WorkOrderService workOrderService,
                                                 IntegrationLogService integrationLogService) {
        this.workOrderService = workOrderService;
        this.integrationLogService = integrationLogService;
    }

    @PostMapping
    @Operation(summary = "Create manufacturing work order from ISM 2")
    public ApiResponse<WorkOrderResponse> createWorkOrder(@Valid @RequestBody CreateWorkOrderRequest request) {
        WorkOrder workOrder = workOrderService.createFromIntegration(request);
        WorkOrderResponse response = workOrderService.toResponse(workOrder);
        ApiResponse<WorkOrderResponse> apiResponse = ApiResponse.ok("Work order created", response);
        integrationLogService.log("ISM2", "CREATE_WORK_ORDER", request.getGoodsRequestNo(), request, apiResponse, "SUCCESS");
        return apiResponse;
    }

    @GetMapping
    @Operation(summary = "List manufacturing work orders")
    public ApiResponse<List<WorkOrderResponse>> listWorkOrders() {
        List<WorkOrderResponse> data = workOrderService.findAll().stream()
                .map(workOrderService::toResponse)
                .toList();
        return ApiResponse.ok("Work orders retrieved", data);
    }

    @GetMapping("/{workOrderNo}/status")
    @Operation(summary = "Retrieve manufacturing work order status")
    public ApiResponse<WorkOrderResponse> getStatus(@PathVariable String workOrderNo) {
        WorkOrder workOrder = workOrderService.findByWorkOrderNo(workOrderNo);
        WorkOrderResponse response = workOrderService.toResponse(workOrder);
        return ApiResponse.ok("Work order status retrieved", response);
    }

    @PutMapping("/{workOrderNo}/status")
    @Operation(summary = "Update manufacturing work order status")
    public ApiResponse<WorkOrderResponse> updateStatus(@PathVariable String workOrderNo,
                                                       @Valid @RequestBody UpdateWorkOrderStatusRequest request) {
        WorkOrder workOrder = workOrderService.updateStatus(workOrderNo, request);
        WorkOrderResponse response = workOrderService.toResponse(workOrder);
        ApiResponse<WorkOrderResponse> apiResponse = ApiResponse.ok("Work order status updated", response);
        integrationLogService.log("SYSTEM_C", "UPDATE_WORK_ORDER_STATUS", workOrderNo, request, apiResponse, "SUCCESS");
        return apiResponse;
    }
}
