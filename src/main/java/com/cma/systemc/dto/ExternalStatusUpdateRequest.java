package com.cma.systemc.dto;

public class ExternalStatusUpdateRequest {
    private String workOrderNo;
    private String goodsRequestNo;
    private String orderNo;
    private String externalReferenceNo;
    private String status;
    private String remarks;

    public String getWorkOrderNo() { return workOrderNo; }
    public void setWorkOrderNo(String workOrderNo) { this.workOrderNo = workOrderNo; }

    public String getGoodsRequestNo() { return goodsRequestNo; }
    public void setGoodsRequestNo(String goodsRequestNo) { this.goodsRequestNo = goodsRequestNo; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getExternalReferenceNo() { return externalReferenceNo; }
    public void setExternalReferenceNo(String externalReferenceNo) { this.externalReferenceNo = externalReferenceNo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
