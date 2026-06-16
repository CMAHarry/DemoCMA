package com.cma.systemc.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_order_materials")
public class WorkOrderMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private RawMaterial material;

    @Column(name = "required_qty", precision = 18, scale = 4, nullable = false)
    private BigDecimal requiredQty;

    @Column(name = "available_qty", precision = 18, scale = 4, nullable = false)
    private BigDecimal availableQty = BigDecimal.ZERO;

    @Column(name = "shortage_qty", precision = 18, scale = 4, nullable = false)
    private BigDecimal shortageQty = BigDecimal.ZERO;

    @Column(name = "restock_required", nullable = false)
    private Boolean restockRequired = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (availableQty == null) availableQty = BigDecimal.ZERO;
        if (shortageQty == null) shortageQty = BigDecimal.ZERO;
        if (restockRequired == null) restockRequired = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public WorkOrder getWorkOrder() { return workOrder; }
    public void setWorkOrder(WorkOrder workOrder) { this.workOrder = workOrder; }

    public RawMaterial getMaterial() { return material; }
    public void setMaterial(RawMaterial material) { this.material = material; }

    public BigDecimal getRequiredQty() { return requiredQty; }
    public void setRequiredQty(BigDecimal requiredQty) { this.requiredQty = requiredQty; }

    public BigDecimal getAvailableQty() { return availableQty; }
    public void setAvailableQty(BigDecimal availableQty) { this.availableQty = availableQty; }

    public BigDecimal getShortageQty() { return shortageQty; }
    public void setShortageQty(BigDecimal shortageQty) { this.shortageQty = shortageQty; }

    public Boolean getRestockRequired() { return restockRequired; }
    public void setRestockRequired(Boolean restockRequired) { this.restockRequired = restockRequired; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
