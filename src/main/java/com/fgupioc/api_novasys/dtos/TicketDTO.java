package com.fgupioc.api_novasys.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 29/07/2026
 */
public record TicketDTO(
        String negocio,          // "MINIMARKET NOVASYS"
        String subtitulo,        // "BOLETA DE VENTA ELECTRÓNICA"
        String serieCorrelativo,  // "B001-0000234"
        String cliente,
        String documento,        // DNI/RUC
        LocalDateTime fecha,
        List<Linea> lineas,
        BigDecimal subtotal,
        BigDecimal igv,
        BigDecimal total,
        BigDecimal recibido,
        BigDecimal vuelto,
        String metodoPago,       // EFECTIVO / YAPE / MIXTO
        String cajero
) {
    public record Linea(int nro, String descripcion, BigDecimal cantidad,
                        BigDecimal precioUnit, BigDecimal importe) {}

    public int itemCount() { return lineas.size(); }
}