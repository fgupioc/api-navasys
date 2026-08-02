package com.fgupioc.api_novasys.models.interfaces;

import java.math.BigDecimal;

public interface IProductoAlmacenList {
    Long getInventoryMovementId();
    String getBarcode();
    String getCategoria();
    Long getCategoryId();
    String getCode();
    String getName();
    String getPriceType();
    Long getProductId();
    BigDecimal getSalePrice();
    Long getStock();
    String getSubCategoria();
    Long getSubcategoryId();
    String getUnidad();
    Long getUnitId();
    String getStatus();
    String getCodStatus();
}
