package com.fgupioc.api_novasys.models.interfaces;

import java.util.UUID;

public interface ProductoAlmacen {
    Long getWarehouseId();
    UUID getWarehouseUuid();
    String getWarehouseName();
    Long getProductId();
    String getCodStatus();
    Double getStock();
}
