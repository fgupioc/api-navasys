package com.fgupioc.api_novasys.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 25/07/2026
 */
@Getter
@Setter
@Builder
public class ItemDetalleDTO {
    private int numero;
    private String codigo;
    private String descripcion;
    private String unidad;
    private BigDecimal cantidad;
    private BigDecimal precioUnit;
    private BigDecimal subtotal;
}