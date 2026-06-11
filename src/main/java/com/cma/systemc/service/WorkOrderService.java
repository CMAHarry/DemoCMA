package com.cma.systemc.service;

import com.cma.systemc.dto.CreateWorkOrderRequest;
import com.cma.systemc.dto.UpdateWorkOrderStatusRequest;
import com.cma.systemc.dto.WorkOrderMaterialResponse;
import com.cma.systemc.dto.WorkOrderResponse;
import com.cma.systemc.entity.*;
import com.cma.systemc.repository.BomItemRepository;
import com.cma.systemc.repository.WorkOrderMaterialRepository;
import com.cma.systemc.repository.WorkOrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkOrderService {
    private static final Logger logger = LogManager.getLogger(WorkOrderService.class);

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderMaterialRepository workOrderMaterialRepository;
    private final BomItemRepository bomItemRepository;
    private final ProductService productService;
    private static final BigDecimal DEMO_MATERIAL_AVAILABLE_QTY = BigDecimal.valueOf(100);

    public WorkOrderService(WorkOrderRepository workOrderRepository,
            WorkOrderMaterialRepository workOrderMaterialRepository,
            BomItemRepository bomItemRepository,
            ProductService productService) {
        this.workOrderRepository = workOrderRepository;
        this.workOrderMaterialRepository = workOrderMaterialRepository;
        this.bomItemRepository = bomItemRepository;
        this.productService = productService;
    }

    public List<WorkOrder> findAll() {
        return workOrderRepository.findAll();
    }

    public WorkOrder findByWorkOrderNo(String workOrderNo) {
        return workOrderRepository.findByWorkOrderNoIgnoreCase(workOrderNo)
                .orElseThrow(() -> new NotFoundException("Work order not found: " + workOrderNo));
    }

    public List<WorkOrderMaterial> findMaterials(WorkOrder workOrder) {
        return workOrderMaterialRepository.findByWorkOrderOrderByMaterialMaterialCodeAsc(workOrder);
    }

    @Transactional
    public WorkOrder createFromIntegration(CreateWorkOrderRequest request) {
        Product product = productService.findByCode(request.getProductCode());
        String workOrderNo = generateWorkOrderNo();

        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderNo(workOrderNo);
        workOrder.setGoodsRequestNo(request.getGoodsRequestNo());
        workOrder.setOrderNo(request.getOrderNo());
        workOrder.setProduct(product);
        workOrder.setQuantity(request.getQuantity());
        workOrder.setExternalReferenceNo(request.getExternalReferenceNo());
        workOrder.setStatus(WorkOrderStatus.RECEIVED);
        workOrder.setRemarks("Work order received from ISM 2");
        workOrder = workOrderRepository.save(workOrder);

        calculateBomRequirements(workOrder);
        logger.info("Created work order [{}] for product [{}] quantity [{}]", workOrderNo, product.getProductCode(),
                request.getQuantity());
        return workOrder;
    }

    @Transactional
    public WorkOrder updateStatus(String workOrderNo, UpdateWorkOrderStatusRequest request) {
        WorkOrder workOrder = findByWorkOrderNo(workOrderNo);
        workOrder.setStatus(request.getStatus());
        workOrder.setRemarks(request.getRemarks());
        logger.info("Updated work order [{}] status to [{}]", workOrderNo, request.getStatus());
        return workOrderRepository.save(workOrder);
    }

    @Transactional
    public void calculateBomRequirements(WorkOrder workOrder) {
        workOrder.setStatus(WorkOrderStatus.CHECKING_BOM);
        workOrderRepository.save(workOrder);

        workOrderMaterialRepository.deleteByWorkOrder(workOrder);
        List<BomItem> bomItems = bomItemRepository.findByProductOrderByMaterialMaterialCodeAsc(workOrder.getProduct());

        if (bomItems.isEmpty()) {
            workOrder.setStatus(WorkOrderStatus.BOM_NOT_FOUND);
            workOrder.setRemarks("No BOM items configured for product " + workOrder.getProduct().getProductCode());
            workOrderRepository.save(workOrder);

            logger.warn("BOM not found for product [{}]", workOrder.getProduct().getProductCode());
            return;
        }

        BigDecimal orderQty = BigDecimal.valueOf(workOrder.getQuantity());
        boolean restockRequired = false;

        for (BomItem bomItem : bomItems) {
            BigDecimal requiredQty = bomItem.getQuantityPerUnit().multiply(orderQty);

            WorkOrderMaterial material = new WorkOrderMaterial();
            material.setWorkOrder(workOrder);
            material.setMaterial(bomItem.getMaterial());
            material.setRequiredQty(requiredQty);
            workOrderMaterialRepository.save(material);

            if (requiredQty.compareTo(DEMO_MATERIAL_AVAILABLE_QTY) > 0) {
                restockRequired = true;

                logger.warn(
                        "RESTOCK REQUIRED | WorkOrder={} | Product={} | Material={} | RequiredQty={} | DemoAvailableQty={}",
                        workOrder.getWorkOrderNo(),
                        workOrder.getProduct().getProductCode(),
                        bomItem.getMaterial().getMaterialCode(),
                        requiredQty,
                        DEMO_MATERIAL_AVAILABLE_QTY);
            } else {
                logger.info(
                        "MATERIAL AVAILABLE | WorkOrder={} | Product={} | Material={} | RequiredQty={} | DemoAvailableQty={}",
                        workOrder.getWorkOrderNo(),
                        workOrder.getProduct().getProductCode(),
                        bomItem.getMaterial().getMaterialCode(),
                        requiredQty,
                        DEMO_MATERIAL_AVAILABLE_QTY);
            }
        }

        workOrder.setStatus(WorkOrderStatus.BOM_AVAILABLE);

        if (restockRequired) {
            workOrder.setRemarks(
                    "BOM calculated. Some raw materials require restock. Demo available quantity is hardcoded as 100.");
            logger.warn("Work order [{}] has material restock requirement.", workOrder.getWorkOrderNo());
        } else {
            workOrder.setRemarks(
                    "BOM requirements calculated successfully. All raw materials are available using demo quantity 100.");
            logger.info("Work order [{}] has enough demo material quantity.", workOrder.getWorkOrderNo());
        }

        workOrderRepository.save(workOrder);
        logger.info("Calculated BOM requirements for work order [{}]", workOrder.getWorkOrderNo());
    }

    public WorkOrderResponse toResponse(WorkOrder workOrder) {
        WorkOrderResponse response = new WorkOrderResponse();
        response.setWorkOrderNo(workOrder.getWorkOrderNo());
        response.setGoodsRequestNo(workOrder.getGoodsRequestNo());
        response.setOrderNo(workOrder.getOrderNo());
        response.setProductCode(workOrder.getProduct().getProductCode());
        response.setProductName(workOrder.getProduct().getProductName());
        response.setQuantity(workOrder.getQuantity());
        response.setStatus(workOrder.getStatus());
        response.setRemarks(workOrder.getRemarks());
        response.setCreatedAt(workOrder.getCreatedAt());
        response.setUpdatedAt(workOrder.getUpdatedAt());

        List<WorkOrderMaterialResponse> materials = findMaterials(workOrder).stream()
                .map(this::toMaterialResponse)
                .collect(Collectors.toList());
        response.setRequiredMaterials(materials);
        return response;
    }

    private WorkOrderMaterialResponse toMaterialResponse(WorkOrderMaterial material) {
        WorkOrderMaterialResponse response = new WorkOrderMaterialResponse();
        response.setMaterialCode(material.getMaterial().getMaterialCode());
        response.setMaterialName(material.getMaterial().getMaterialName());
        response.setUnitOfMeasure(material.getMaterial().getUnitOfMeasure());
        response.setRequiredQty(material.getRequiredQty());
        return response;
    }

    private String generateWorkOrderNo() {
        String datePart = java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long sequence = System.currentTimeMillis() % 100000;
        return "WO-" + datePart + "-" + sequence;
    }
}
