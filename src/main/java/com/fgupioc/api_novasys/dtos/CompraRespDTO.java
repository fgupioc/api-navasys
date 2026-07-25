package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.Almacen;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 24/07/2026
 */
public record CompraRespDTO(
        Long id,
        UUID uuid,
        String code,
        ProveedorRespDTO supplier,
        AlmacenRespDTO warehouse,
        LocalDate purchaseDate,
        String documentType,
        String documentSeries,
        String documentNumber,
        String paymentType,
        String currency,
        String observation,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal tax,
        BigDecimal total,
        String codStatus,
        Long createdBy,
        List<CompraProducto> detalles
) {

}
