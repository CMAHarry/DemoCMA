package com.cma.systemc.dto;

import java.math.BigDecimal;

public class WorkOrderMaterialResponse {
    private String materialCode;
    private String materialName;
    private String unitOfMeasure;
    private BigDecimal requiredQty;
    private BigDecimal availableQty;
    private BigDecimal shortageQty;
    private Boolean restockRequired;

    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public BigDecimal getRequiredQty() { return requiredQty; }
    public void setRequiredQty(BigDecimal requiredQty) { this.requiredQty = requiredQty; }

    public BigDecimal getAvailableQty() { return availableQty; }
    public void setAvailableQty(BigDecimal availableQty) { this.availableQty = availableQty; }

    public BigDecimal getShortageQty() { return shortageQty; }
    public void setShortageQty(BigDecimal shortageQty) { this.shortageQty = shortageQty; }

    public Boolean getRestockRequired() { return restockRequired; }
    public void setRestockRequired(Boolean restockRequired) { this.restockRequired = restockRequired; }
}
