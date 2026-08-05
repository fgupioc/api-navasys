package com.fgupioc.api_novasys.dtos;

import java.math.BigDecimal;
import java.util.List;

/**
 * Datos planos que consume la plantilla del ticket de cierre de caja (58 mm).
 * Todo lo que sea formato de fecha / etiquetas se resuelve antes de llegar aqui.
 */
public record TicketCierreCajaData(
        // Cabecera
        String nombreComercial,
        String razonSocial,
        String ruc,
        String direccionSucursal,
        String numeroCierre,

        // Caja
        String caja,
        String sucursal,
        String fechaApertura,
        String fechaCierre,
        String duracion,

        // Responsable
        String usuarioNombre,
        String usuarioDocumento,
        String usuarioRol,
        String turno,

        // Ventas
        int cantidadVentas,
        BigDecimal totalVentas,
        List<MedioPago> mediosPago,

        // Arqueo
        BigDecimal fondoInicial,
        BigDecimal ventasEfectivo,
        BigDecimal ingresos,
        BigDecimal egresos,
        BigDecimal efectivoEsperado,
        BigDecimal efectivoContado,
        BigDecimal diferencia,
        List<Denominacion> denominaciones,

        String observaciones,
        String fechaEmision
) {

    public record MedioPago(String nombre, BigDecimal monto) {}

    public record Denominacion(BigDecimal valor, int cantidad, BigDecimal subtotal) {}

    /** Etiqueta que se imprime debajo del monto de diferencia. */
    public String etiquetaDiferencia() {
        int signo = diferencia.compareTo(BigDecimal.ZERO);
        if (signo == 0) return "*** CAJA CUADRADA ***";
        return signo < 0 ? "*** FALTANTE EN CAJA ***" : "*** SOBRANTE EN CAJA ***";
    }
}