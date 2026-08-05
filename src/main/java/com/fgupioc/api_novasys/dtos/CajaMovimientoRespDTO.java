package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.enums.TipoMovimientoCaja;
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
 * @since 05/08/2026
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CajaMovimientoRespDTO {
    private Long id;
    private TipoMovimientoCaja type;
    private String reason;
    private String description;
    private BigDecimal amount;
    private String usuario;
    private LocalDateTime movementDate;
}
