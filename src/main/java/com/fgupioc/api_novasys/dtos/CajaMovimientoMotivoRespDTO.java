package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.enums.TipoMovimientoCaja;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class CajaMovimientoMotivoRespDTO {
    private Long id;
    private TipoMovimientoCaja movementType;
    private String name;
    private String description;
    private Boolean active;
    private Integer sortOrder;
}
