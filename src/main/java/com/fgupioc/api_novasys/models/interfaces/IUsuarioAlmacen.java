package com.fgupioc.api_novasys.models.interfaces;

public interface IUsuarioAlmacen {
    Boolean getIsDefault();

    Long getUserWarehouseId();

    String getWarehouseCode();

    String getWarehouseDescription();

    String getWarehouseName();

    String getWarehouseUuid();
}
