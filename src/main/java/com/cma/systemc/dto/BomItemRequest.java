package com.cma.systemc.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BomItemRequest {
    @NotBlank
    private String productCode;

    @NotBlank
    private String materialCode;

    @NotNull
    @DecimalMin("0.0001")
    private BigDecimal quantityPerUnit;

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }

    public BigDecimal getQuantityPerUnit() { return quantityPerUnit; }
    public void setQuantityPerUnit(BigDecimal quantityPerUnit) { this.quantityPerUnit = quantityPerUnit; }
}
