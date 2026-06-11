package com.cma.systemc.entity;

public enum WorkOrderStatus {
    RECEIVED,
    CHECKING_BOM,
    BOM_AVAILABLE,
    BOM_NOT_FOUND,
    IN_PRODUCTION,
    COMPLETED,
    FAILED
}
