package com.fgupioc.api_novasys.dtos;

import java.math.BigDecimal;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 24/07/2026
 */
public record CompraProducto(
        Long id,
        String barcode,
        String categoria,
        String code,
        String name,
        String subCategoria,
        String unidad,
        String url,
        Double quantity,
        BigDecimal unitCost,
        BigDecimal salePrice,
        BigDecimal discount
) {
}
