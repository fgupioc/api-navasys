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
