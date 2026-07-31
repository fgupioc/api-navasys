package com.fgupioc.api_novasys.models.interfaces;

public interface ProductoAlmacen {
    Long getWarehouseId();
    String getWarehouseName();
    Long getProductId();
    String getCodStatus();
    Double getStock();
}
