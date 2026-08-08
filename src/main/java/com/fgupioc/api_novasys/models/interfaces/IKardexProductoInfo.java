package com.fgupioc.api_novasys.models.interfaces;

import java.math.BigDecimal;

public interface IKardexProductoInfo {
    Long getProductId();

    String getProductName();

    String getProductCode();

    String getUnitName();

    String getUnitCode();

    String getWarehouseName();

    String getUrl();

    Double getInitialQuantity();

    Double getTotalEntries();

    Double getCurrentQuantity();

    BigDecimal getSalePrice();

    BigDecimal getRegularPrice();

    BigDecimal getWholesalePrice();

    Double getSoldQuantity();

    BigDecimal getTotalValue();
}
