package com.fgupioc.api_novasys.models.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 24/07/2026
 */
public interface CompraListado {
    UUID getUuid();

    String getDocumentSeries();

    String getDocumentNumber();

    LocalDate getPurchaseDate();

    BigDecimal getTotal();

    String getCodStatus();

    String getStatus();

    String getSupplier();
}
