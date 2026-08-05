package com.fgupioc.api_novasys.models.interfaces;

import java.math.BigDecimal;

public interface IVentasDiarias {
    Long getId();
    String getMethod();
    BigDecimal getTotal();
    BigDecimal getEfectivo();
    BigDecimal getYape();
}
