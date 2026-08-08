package com.fgupioc.api_novasys.models.interfaces;

import com.fgupioc.api_novasys.models.enums.InventarioReferenciaTipo;

import java.math.BigDecimal;

public interface IHistorialKardex {
    InventarioReferenciaTipo getReferenceType();

    String getCreatedAt();

    String getDocument();

    String getType();

    String getReference();

    BigDecimal getEntries();

    BigDecimal getExits();

    BigDecimal getUnitCost();

    BigDecimal getTotalCost();

    String getUserName();
}
