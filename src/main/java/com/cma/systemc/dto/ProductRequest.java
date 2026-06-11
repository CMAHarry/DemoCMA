package com.cma.systemc.dto;

import jakarta.validation.constraints.NotBlank;

public class ProductRequest {
    @NotBlank
    private String productCode;

    @NotBlank
    private String productName;

    private String description;
    private Boolean active = true;

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
