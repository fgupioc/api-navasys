package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.CompraDetalle;

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
    public static CompraProducto toDetalleDTO(CompraDetalle compraDetalle) {
        return new CompraProducto(
                compraDetalle.getId(),
                compraDetalle.getProducto().getBarcode(),
                compraDetalle.getProducto().getCategoria().getName(),
                compraDetalle.getProducto().getCode(),
                compraDetalle.getProducto().getName(),
                compraDetalle.getProducto().getSubcategoria().getName(),
                compraDetalle.getProducto().getUnidad().getName(),
                compraDetalle.getProducto().getImagenes().isEmpty() ? "" : compraDetalle.getProducto().getImagenes().getFirst().getUrl(),
                compraDetalle.getQuantity(),
                compraDetalle.getUnitCost(),
                compraDetalle.getSalePrice(),
                compraDetalle.getDiscount()
        );
    }
}
