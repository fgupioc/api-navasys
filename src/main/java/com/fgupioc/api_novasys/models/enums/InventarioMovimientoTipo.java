package com.fgupioc.api_novasys.models.enums;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 24/07/2026
 */
public enum InventarioMovimientoTipo {
    PURCHASE_IN, // Compra
    PURCHASE_RETURN, // Devolución de compra
    SALE_OUT, // Venta
    SALE_RETURN, // Devolución de venta
    TRANSFER_IN, // Transferencia entrante
    TRANSFER_OUT, // Transferencia saliente
    ADJUSTMENT_IN, // Ajuste entrante
    ADJUSTMENT_OUT // Ajuste saliente
}
