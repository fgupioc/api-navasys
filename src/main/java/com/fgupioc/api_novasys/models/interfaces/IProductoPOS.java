package com.fgupioc.api_novasys.models.interfaces;

import java.util.UUID;

public interface IProductoPOS {
    String getBarcode();

    String getCategoria();

    Long getCategoryId();

    String getCode();

    Long getInventoryStockId();

    String getProductName();

    Long getProductId();

    Double getRegularPrice();

    Double getSalePrice();

    Long getStock();

    String getSubCategoria();

    Long getSubcategoryId();

    String getUnidad();

    Long getUnitId();

    UUID getUuid();

    String getWarehouseName();

    UUID getWarehouseUuid();

    Integer getWholesaleMinimumQuantity();

    Double getWholesalePrice();

    String getUrl();
}
