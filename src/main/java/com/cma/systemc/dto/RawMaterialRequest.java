package com.cma.systemc.dto;

import jakarta.validation.constraints.NotBlank;

public class RawMaterialRequest {
    @NotBlank
    private String materialCode;

    @NotBlank
    private String materialName;

    private String unitOfMeasure;
    private Boolean active = true;

    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
