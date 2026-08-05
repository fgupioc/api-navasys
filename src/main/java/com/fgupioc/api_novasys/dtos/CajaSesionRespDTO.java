package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.enums.EstadoSesionCaja;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class CajaSesionRespDTO {
    private Long cajaSesionId;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private BigDecimal openingAmount;
    private BigDecimal closingAmount;
    private EstadoSesionCaja status;
    private String notes;

    private UUID cajaUuid;
    private Integer salesCount;
    private BigDecimal collectionsAmount;
    private Integer movementsCount;
    private BigDecimal physicalCashAmount;
    private BigDecimal executiveCashAmount;
    private BigDecimal digitalWalletAmount;
    private BigDecimal totalPaymentsAmount;
    private BigDecimal cashInAmount;
    private BigDecimal cashOutAmount;
    private BigDecimal cashSalesAmount;
    private BigDecimal expectedCashAmount;
    private BigDecimal countedCashAmount;
    private BigDecimal cashDifferenceAmount;

}
