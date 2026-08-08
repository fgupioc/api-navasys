package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.Almacen;
import com.fgupioc.api_novasys.models.Proveedor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 24/07/2026
 */
public record CompraReqDTO(
        String code,
        Long supplierId,
        Long warehouseId,
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
        List<CompraProducto> products
) {

    public BigDecimal calcularTotal() {
        return products.stream().reduce(BigDecimal.ZERO, (subtotal1, product) -> subtotal1.add(product.unitCost().multiply(product.quantity())), BigDecimal::add);
    }
}
