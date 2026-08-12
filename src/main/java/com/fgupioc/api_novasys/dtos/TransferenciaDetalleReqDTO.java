package com.fgupioc.api_novasys.dtos;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 09/08/2026
 */

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaDetalleReqDTO {
    private Long productoId;
    private BigDecimal quantity;
}
