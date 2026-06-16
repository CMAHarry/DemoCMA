package com.cma.systemc.api;

import com.cma.systemc.dto.ApiResponse;
import com.cma.systemc.dto.ExternalStatusUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock/ism2")
@Tag(name = "Mock ISM 2 API", description = "Demo endpoint that simulates ISM 2 receiving status updates from System C")
public class MockIsm2ApiController {
    private static final Logger logger = LogManager.getLogger(MockIsm2ApiController.class);

    @PostMapping("/order-product-status")
    @Operation(summary = "Mock ISM 2 endpoint to receive work order status update")
    public ApiResponse<ExternalStatusUpdateRequest> receiveStatusUpdate(@RequestBody ExternalStatusUpdateRequest request) {
        logger.info("MOCK ISM2 RECEIVED STATUS | WorkOrder={} | ExternalRef={} | Status={} | Remarks={}",
                request.getWorkOrderNo(),
                request.getExternalReferenceNo(),
                request.getStatus(),
                request.getRemarks());

        return ApiResponse.ok("Mock ISM 2 accepted status update", request);
    }
}
