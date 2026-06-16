package com.cma.systemc.entity;

public enum WorkOrderStatus {
    RECEIVED,
    CHECKING_BOM,
    BOM_AVAILABLE,
    BOM_NOT_FOUND,

    MATERIAL_SHORTAGE,
    RESTOCK_IN_PROGRESS,
    READY_FOR_PRODUCTION,

    IN_PRODUCTION,
    COMPLETED,
    FAILED
}
