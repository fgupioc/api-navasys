package com.fgupioc.api_novasys.dtos;

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
public class CajaSesionReqDTO {
    private UUID cajaUuid;
    private BigDecimal openingAmount;
    private BigDecimal closingAmount;
    private String notes;
}
