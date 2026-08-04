package com.fgupioc.api_novasys.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class CajaSesionReqDTO {
    private UUID cajaUuid;
    private BigDecimal openingAmount;
    private BigDecimal closingAmount;
    private String notes;

    private Long cajaSesionId;
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
