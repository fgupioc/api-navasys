package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.enums.EstadoSesionCaja;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 03/08/2026
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CajaSesionRespDTO {
    private Long cajaSesionId;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private BigDecimal openingAmount;
    private BigDecimal closingAmount;
    private EstadoSesionCaja status;
    private String notes;

    private BigDecimal ventas;
    private BigDecimal cobros;
    private BigDecimal movimientos;
    private BigDecimal montoYape;
    private BigDecimal montoMixto;
    private BigDecimal ingresos;
    private BigDecimal egresos;
    private BigDecimal ventasEfectivo;
    private BigDecimal montoEfectivo;
    private BigDecimal fondoInicial;
}
