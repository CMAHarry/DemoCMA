package com.cma.systemc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateWorkOrderRequest {
    @NotBlank
    private String goodsRequestNo;

    @NotBlank
    private String orderNo;

    @NotBlank
    private String productCode;

    @NotNull
    @Min(1)
    private Integer quantity;

    private String externalReferenceNo;

    public String getGoodsRequestNo() { return goodsRequestNo; }
    public void setGoodsRequestNo(String goodsRequestNo) { this.goodsRequestNo = goodsRequestNo; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getExternalReferenceNo() { return externalReferenceNo; }
    public void setExternalReferenceNo(String externalReferenceNo) { this.externalReferenceNo = externalReferenceNo; }
}
